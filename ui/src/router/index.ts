import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'

export const menuRoutes: RouteRecordRaw[] = [
	{
		path: '/p/mock',
		meta: {
			title: 'Mock-Server',
			icon: 'icon-appstore'
		},
		children: [
			{
				path: '/mock/api',
				name: 'api',
				component: () => import('../views/mock/api/index.vue'),
				meta: {
					title: 'api list',
					icon: 'icon-fire'
				}
			},
			{
				path: '/mock/attachment',
				name: 'attachment',
				component: () => import('../views/mock/attachment/index.vue'),
				meta: {
					title: 'attachment list',
					icon: 'icon-file'
				}
			}
		]
	}
]

export const constantRoutes: RouteRecordRaw[] = [
	{
		path: '/redirect',
		component: () => import('../layout/index.vue'),
		children: [
			{
				path: '/redirect/:path(.*)',
				component: () => import('../layout/components/Router/Redirect.vue')
			}
		]
	},
	{
		path: '/',
		component: () => import('../layout/index.vue'),
		redirect: '/mock/api',
		children: [...menuRoutes]
	},
	{
		path: '/404',
		component: () => import('../views/404.vue')
	},
	{
		path: '/:pathMatch(.*)',
		redirect: '/404'
	}
]

export const router = createRouter({
	history: createWebHashHistory(),
	routes: constantRoutes
})
