/*
 * Copyright (c) 2026 Huawei Technologies Co., Ltd.
 * openGauss-tools-monitor is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
