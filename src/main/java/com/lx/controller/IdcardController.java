package com.lx.controller;


import com.aliyun.ocr_api20210707.models.RecognizeIdcardResponse;
import com.aliyun.ocr_api20210707.models.RecognizeIdcardResponseBody;
import com.aliyun.tea.TeaException;
import com.lx.annotation.AuthAccess;
import com.lx.dto.IdCardDataType;
import com.lx.dto.IdCardRoot;
import com.lx.dto.OCRResponse;
import com.lx.utils.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘肖
 * @since 2023-11-20
 */
@RestController
@RequestMapping("/idCard")
@Slf4j
public class IdcardController {

//    private SimpleDateFormat simpleDateFormat;
//
//    {
//        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日/");
//    }

    @Value("${file-save-path}")
    private String fileSavePath;

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.ocr_api20210707.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/ocr-api
        config.endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";
        return new com.aliyun.ocr_api20210707.Client(config);
    }

    @AuthAccess
    @SneakyThrows
    @PostMapping("/ocr")
    public R OCR(@RequestParam("file") MultipartFile file, HttpServletRequest request)
    {
        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.ocr_api20210707.Client client = IdcardController.createClient("","");

        java.io.InputStream bodyStream = file.getInputStream();

        com.aliyun.ocr_api20210707.models.RecognizeIdcardRequest recognizeIdcardRequest = new com.aliyun.ocr_api20210707.models.RecognizeIdcardRequest().setBody(bodyStream);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

//        String directory = simpleDateFormat.format(new Date());
        /**
         *  2.文件保存目录  D: IDEAProjects/images
         *  如果目录不存在，则创建
         */
        File dir = new File(fileSavePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        log.info("图片上传，保存位置：" + fileSavePath);
        //3.给文件重新设置一个名字
        //后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName= UUID.randomUUID().toString().replaceAll("-", "")+suffix;
        //4.创建这个新文件
        File newFile = new File(fileSavePath + newFileName);

        try {
            file.transferTo(newFile);
            //协议 :// ip地址 ：端口号 / 文件目录(/images/2023年11月27日/xxx.jpg)
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/" + newFileName;
            log.info("图片上传，访问URL：" + url);

            // 复制代码运行请自行打印 API 的返回值
            RecognizeIdcardResponse response = client.recognizeIdcardWithOptions(recognizeIdcardRequest, runtime);
            RecognizeIdcardResponseBody body = response.getBody();

            String data = body.getData();
            IdCardRoot idCardRoot = JSON.parseObject(data, IdCardRoot.class);

            OCRResponse ocrResponse = new OCRResponse(idCardRoot, url);

            return R.success(ocrResponse, "上传成功");
        } catch (TeaException error) {
//             如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
            error.printStackTrace();
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
//            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
            _error.printStackTrace();
        }
        return R.error("上传失败，请检查图片是否有效");
    }
}
