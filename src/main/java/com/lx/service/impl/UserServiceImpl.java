package com.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lx.entity.*;
import com.lx.mapper.MetricsMapper;
import com.lx.pojo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lx.dto.*;
import com.lx.mapper.UserMapper;
import com.lx.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.utils.R;
import com.lx.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘肖
 * @since 2023-11-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MetricsMapper metricsMapper;

    @Autowired
    private IIdcardService idCardService;

    @Autowired
    private IEducationService educationService;

    @Autowired
    private IEmploymentService employmentService;

    @Autowired
    private IMetricsService metricsService;

    @Autowired
    private IVariationService variationService;

    @Autowired
    JavaMailSender javaMailSender; //可直接注入邮件发送的对象

    @Value("${spring.mail.username}") //动态获取配置文件中 发送邮件的账户
    private String sender;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 5;

    private static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }


    private User getUserByPhone(String phone)
    {
        try
        {
            LambdaQueryWrapper<User> queryUser = new LambdaQueryWrapper<>();
            queryUser.eq(User::getPhone, phone);
            return userMapper.selectOne(queryUser);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private UserInfo getUserInfo(User user)
    {
        Idcard idCard = idCardService.getById(user.getIdNo());
        Education education = educationService.getById(idCard.getIdNo());
        Employment employment = employmentService.getById(idCard.getIdNo());
        return new UserInfo(user, idCard, education, employment);
    }

    @Override
    public R sendEmailCode(String email)
    {
        try
        {
            String code = generateRandomCode();
            //创建简单邮件消息
            SimpleMailMessage message = new SimpleMailMessage();
            //谁发的
            message.setFrom(sender);
            //谁要接收
            message.setTo(email);
            //邮件标题
            message.setSubject("CNMOCRS 验证码");
            //邮件内容
            message.setText("您的验证码是 "+ code);
            javaMailSender.send(message);
            return R.success(code, "发送成功，请注意查收");
        }
        catch (Exception e)
        {
            return R.error("发送失败");
        }
    }

    @Override
    public R register(RequestObject request)
    {
        try
        {
            // 获取前端发送的注册信息并初始化
            Form form = request.getRegisterForm();

            IdCardDataType uploadData = request.getUploadData();
            IdCardFace idCardFace = uploadData.getFace();
            IdCardBack idCardBack = uploadData.getBack();
            IdCardDataFaceData idCardDataFaceData = idCardFace.getData();
            IdCardDataBackData idCardDataBackData = idCardBack.getData();

            Education education = request.getEducation();
            Employment employment = request.getEmployment();

            User user = new User(idCardDataFaceData.getIdNumber(), form.getPhone(), form.getPassword(), form.getEmail());
            Idcard idCard = new Idcard(idCardDataFaceData.getIdNumber(), idCardDataFaceData.getName(),
                    idCardDataFaceData.getSex(), idCardDataFaceData.getEthnicity(), idCardDataFaceData.getBirthDate(),
                    idCardDataFaceData.getAddress());

            // 判断信息是否存在
            R idCardR = idCardService.saveIdCard(idCard);
            R userR = saveUser(user);

            // 将用户信息和身份证信息存入数据库
            if (idCardR.getCode() == 200 && userR.getCode() == 200)
            {
                idCardService.save(idCard);
                save(user);
                // 如果用户注册时没跳过则将education和employment存入数据库, 否则什么也不做
                if (education != null && employment != null)
                {
                    education.setIdNo(user.getIdNo());
                    educationService.save(education);

                    employment.setIdNo(user.getIdNo());
                    employmentService.save(employment);
                }
                UserInfo userInfo = new UserInfo(user, idCard, education, employment);
                return R.success(userInfo,"注册成功");
            }
            else if (userR.getCode() == 0)
            {
                return R.error(userR.getMsg());// 手机号已存在
            }
            else if (idCardR.getCode() == 0)
            {
                return R.error(idCardR.getMsg());// 身份证号已存在
            }
            else return R.error("注册失败");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("注册失败");
        }
    }

    @Override
    public R login(Form form)
    {
        try
        {
            LambdaQueryWrapper<User> queryUser = new LambdaQueryWrapper<>();
            queryUser.eq(User::getPhone, form.getPhone());
            User user = userMapper.selectOne(queryUser);
            if (user == null)
            {
                return R.error("手机号不存在");
//                throw new ServiceException("手机号不存在");
            }
            else
            {
                if (!user.getPassword().equals(form.getPassword()))
                {
                    return R.error("密码错误");
                }
                else
                {
                    if (user.getDays() == 0)
                    {
                        this.getScore(user.getIdNo());
                        user = userMapper.selectOne(queryUser);
                    }

                    // 每次登录天数加一
                    user.setDays(user.getDays() + 1);
                    //user.setScore(user.getScore());
                    System.out.println("----------------------"+user.getScore());
                    userMapper.updateById(user);
                    // 生成token
                    String token = TokenUtils.createToken(user.getPhone(), user.getPassword());
                    user.setToken(token);

                    LoginResult data = new LoginResult(user.getPhone(), user.getToken(), user.getDays());
                    return R.success(data, "登录成功");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("登录失败, 请检查账号或密码是否正确");
        }
    }

    @Override
    public R reset(ResetForm resetForm)
    {
        try
        {
            // 判断用户信息是否正确
            User user = getUserByPhone(resetForm.getPhone());
            if (user == null)
            {
                return R.error("手机号不存在");
            }
            else
            {
                Idcard idCard = idCardService.getById(resetForm.getIdNo());
                if (idCard == null)
                {
                    return R.error("身份证号不存在");
                }
                else
                {
                    if (!idCard.getName().equals(resetForm.getName()))
                    {
                        return R.error("姓名错误");
                    }
                    else
                    {
                        // 信息正确时做密码更新操作
                        user.setPassword(resetForm.getNewPassword());
                        userMapper.updateById(user);
                        return R.success(getUserInfo(user), "重置成功");
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("重置失败");
        }
    }

    @Override
    public R saveUser(User user)
    {
        try
        {
            User u = getUserByPhone(user.getPhone());
            if (u == null)
            {
                return R.success(user, "注册成功");
            }
            else
            {
                return R.error("注册失败，手机号已存在");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("注册失败");
        }
    }

    @Override
    public R getUser(String phone)
    {
        try
        {
            LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
            userQuery.eq(User::getPhone, phone);
            User user = userMapper.selectOne(userQuery);
            if (user != null)
            {
                return R.success(getUserInfo(user), "获取成功");
            }
            else
            {
                return R.error("获取失败");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("获取失败");
        }
    }

    @Override
    public R update(User user)
    {
        try
        {
            user.setModifyTimes(user.getModifyTimes() + 1);
            this.getScore(user.getIdNo());
//            userMapper.updateById(user);
            return R.success(user, "修改成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("修改失败");
        }
    }

    @Override
    public R getScore(String idNo)
    {
        try
        {
            System.out.println("666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
            Integer score = 0;
            Metrics metrics = new Metrics();
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Idcard> idCardQueryWrapper = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Education> educationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Employment> employmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getIdNo, idNo);
            idCardQueryWrapper.eq(Idcard::getIdNo, idNo);
            educationLambdaQueryWrapper.eq(Education::getIdNo, idNo);
            employmentLambdaQueryWrapper.eq(Employment::getIdNo, idNo);
            User user = userMapper.selectOne(userLambdaQueryWrapper);
            Idcard idcard = idCardService.getOne(idCardQueryWrapper);
            Education education = educationService.getOne(educationLambdaQueryWrapper);
            Employment employment = employmentService.getOne(employmentLambdaQueryWrapper);
            if (employment == null && education == null)
            {
                score += 100;
                metrics.setAccount(100);
            }
            else if (employment == null || education == null)
            {
                score += 150;
                metrics.setAccount(150);
            }
            else
            {
                score += 200;
                metrics.setAccount(200);

                if ("待业".equals(employment.getStatus()))
                {
                    score += 0;
                    metrics.setAbility(0);
                }
                else
                {
                    if ("学生".equals(employment.getType()))
                    {
                        score += 100;
                        metrics.setAbility(100);
                    }
                    else if ("退休/无业".equals(employment.getType()))
                    {
                        score += 50;
                        metrics.setAbility(50);
                    }
                    else
                    {
                        if ("研究生".equals(education.getDegree()))
                        {
                            if ("博士".equals(education.getTitle()))
                            {
                                score += 200;
                                metrics.setAbility(200);
                            }
                            else
                            {
                                score += 170;
                                metrics.setAbility(170);
                            }
                        }
                        else if ("本科".equals(education.getDegree()))
                        {
                            score += 150;
                            metrics.setAbility(150);
                        }
                        else if ("大专".equals(education.getDegree()))
                        {
                            score += 120;
                            metrics.setAbility(120);
                        }
                        else if ("高中".equals(education.getDegree()))
                        {
                            score += 90;
                            metrics.setAbility(90);
                        }
                        else if ("初中".equals(education.getDegree()))
                        {
                            score += 50;
                            metrics.setAbility(50);
                        }
                        else if ("小学".equals(education.getDegree()))
                        {
                            score += 20;
                            metrics.setAbility(20);
                        }
                    }
                }
            }

            if ("研究生".equals(education.getDegree()))
            {
                if ("博士".equals(education.getTitle()))
                {
                    score += 200;
                    metrics.setUser(200);
                }
                else
                {
                    score += 170;
                    metrics.setUser(170);
                }
            }
            else if ("本科".equals(education.getDegree()))
            {
                score += 150;
                metrics.setUser(150);
            }
            else if ("大专".equals(education.getDegree()))
            {
                score += 120;
                metrics.setUser(120);
            }
            else if ("高中".equals(education.getDegree()))
            {
                score += 90;
                metrics.setUser(90);
            }
            else if ("初中".equals(education.getDegree()))
            {
                score += 50;
                metrics.setUser(50);
            }
            else if ("小学".equals(education.getDegree()))
            {
                score += 20;
                metrics.setUser(20);
            }


            if (user.getDays() >= 30)
            {
                score += 200;
                metrics.setActivity(200);
            }
            if (user.getDays() >= 15)
            {
                score += 150;
                metrics.setActivity(150);
            }
            else if (user.getDays() >= 7)
            {
                score += 100;
                metrics.setActivity(100);
            }
            else if (user.getDays() >= 3)
            {
                score += 50;
                metrics.setActivity(50);
            }
            else
            {
                score += 0;
                metrics.setActivity(0);
            }

            if (user.getModifyTimes() >= 20)
            {
                score += 0;
                metrics.setBehavior(0);
            }
            else if (user.getModifyTimes() >= 15)
            {
                score += 50;
                metrics.setBehavior(50);
            }
            else if (user.getModifyTimes() >= 10)
            {
                score += 100;
                metrics.setBehavior(100);
            }
            else if (user.getModifyTimes() >= 8)
            {
                score += 150;
                metrics.setBehavior(150);
            }
            else if (user.getModifyTimes() >= 5)
            {
                score += 180;
                metrics.setBehavior(180);
            }
            else
            {
                score += 200;
                metrics.setBehavior(200);
            }

            metrics.setScore(score);
            metrics.setIdNo(user.getIdNo());


            user.setScore(score);
            user.setPhone(user.getPhone());
            //user.setDays(user.getDays()+1);

            userMapper.updateById(user);
            System.out.println(user);
            System.out.println("user.getScore() = " + user.getScore());
//            System.out.println(user.getPhone());
//            System.out.println("666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
//
//            System.out.println(user.getIdNo());
//            System.out.println("666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
//
//            System.out.println(user.getPassword());
//            System.out.println(user.getScore());
            LambdaUpdateWrapper<Metrics> metricsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            metricsLambdaUpdateWrapper.eq(Metrics::getIdNo, idNo);
            Metrics metrics0 = metricsMapper.selectOne(metricsLambdaUpdateWrapper);
            System.out.println("------------------------------------------"+metrics0);
            if (metrics0 == null) {
                metricsMapper.insert(metrics);
                System.out.println("添加"+user);
            }else{
                metrics.setId(metrics0.getId());
                metricsMapper.updateById(metrics);
                System.out.println("修改"+metrics0);
            }

            variationService.save(idNo);
            return R.success(metrics, "评估成功");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("评估失败");
        }
    }
}
