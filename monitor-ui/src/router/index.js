import { createRouter, createWebHashHistory } from 'vue-router'
import exporterConfiguration from '../views/exporterConfiguration.vue'

const routes = [
  {
    path: "/",
    name: "exporterConfiguration",
    redirect: '/configurationCenter',
    component: exporterConfiguration,
    children: [
        {
            path: "/configurationCenter",
            name: "configurationCenter",
            component: () => import ( /* webpackChunkName: "dashboard" */ "@/views/configurationCenter.vue")
        }, {
            path: "/fileGeneration",
            name: "fileGeneration",
            component: () => import ( /* webpackChunkName: "table" */ "@/views/fileGeneration.vue")
        }, {
            path: "/IndexRelease",
            name: "IndexRelease",
            component: () => import ( /* webpackChunkName: "table" */ "@/views/IndexRelease.vue")
        }
    ]
  },{
    path: "/:catchAll(.*)", 
    redirect: '/404',
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
