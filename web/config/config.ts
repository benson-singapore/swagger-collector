// https://umijs.org/config/
import { defineConfig } from 'umi';
import defaultSettings from './defaultSettings';
import proxy from './proxy';

const { REACT_APP_ENV } = process.env;

export default defineConfig({
  hash: true,
  antd: {},
  dva: {
    hmr: true,
  },
  layout: {
    name: 'Ant Design Pro',
    locale: true,
    siderWidth: 208,
  },
  locale: {
    // default zh-CN
    default: 'zh-CN',
    // default true, when it is true, will use `navigator.language` overwrite default
    antd: true,
    baseNavigator: true,
  },
  dynamicImport: {
    loading: '@/components/PageLoading/index',
  },
  targets: {
    ie: 11,
  },
  // umi routes: https://umijs.org/docs/routing
  routes: [
    {
      path: '/user',
      layout: false,
      routes: [
        {
          name: 'login',
          path: '/user/login',
          component: './user/login',
        },
      ],
    },
    {
      path: '/api/swagger',
      name: 'Swagger',
      icon: 'ApiOutlined',
      component: './SwaggerPage',
    },
    {
      path: '/api/config',
      name: '接口配置',
      icon: 'setting',
      component: './ApiConfigList',
    },
    {
      path: '/api/auto/auth',
      name: '自动授权',
      icon: 'SafetyCertificateOutlined',
      component: './AutoAuthList',
    },
    {
      path: '/api/config/detail/:apiConfigId/:apiConfigName',
      hidden: true,
      component: './ApiConfigList/components/ApiConfigDetailList',
    },
    {
      path: '/welcome',
      hidden: true,
      component: './SwaggerPage',
      // component: './Welcome',
    },
    {
      path: '/',
      redirect: '/api/swagger',
    },
    {
      component: './404',
    },
  ],
  // Theme for antd: https://ant.design/docs/react/customize-theme-cn
  theme: {
    // ...darkTheme,
    'primary-color': defaultSettings.primaryColor,
  },
  // @ts-ignore
  title: false,
  ignoreMomentLocale: true,
  proxy: proxy[REACT_APP_ENV || 'dev'],
  manifest: {
    basePath: '/',
  },
});
