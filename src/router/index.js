import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeIndex from '@/views/HomeIndex'
import MyCredit from '@/views/user/MyCredit'
import LoginAgreement from '@/views/LoginAgreement'
import RegisterIndex from '@/views/register/RegisterIndex'
import ResetPassword from '@/views/reset/ResetPassword'
import UserIndex from '@/views/user/UserIndex'

Vue.use(VueRouter)

const routes = [
  { path: '/', name: 'home', component: HomeIndex },
  { path: '/myCredit', name: 'myCredit', component: MyCredit },
  { path: '/agreement', name: 'agreement', component: LoginAgreement },
  { path: '/register', name: 'register', component: RegisterIndex },
  { path: '/reset', name: 'reset', component: ResetPassword },
  { path: '/user', name: 'user', component: UserIndex }
]

const router = new VueRouter({
  routes
})

export default router
