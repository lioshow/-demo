import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeIndex from '@/views/HomeIndex'
import MyCredit from '@/views/notLogin/MyCredit'
import LoginAgreement from '@/views/LoginAgreement'
import RegisterIndex from '@/views/register/RegisterIndex'

Vue.use(VueRouter)

const routes = [
  { path: '/', name: 'home', component: HomeIndex },
  { path: '/myCredit', name: 'myCredit', component: MyCredit },
  { path: '/agreement', name: 'agreement', component: LoginAgreement },
  { path: '/register', name: 'register', component: RegisterIndex }
]

const router = new VueRouter({
  routes
})

export default router
