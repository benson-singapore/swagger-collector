import React from 'react';
import { BasicLayoutProps, Settings as LayoutSettings } from '@ant-design/pro-layout';
import { notification } from 'antd';
import { history, RequestConfig } from 'umi';
import RightContent from '@/components/RightContent';
import Footer from '@/components/Footer';
import { ResponseError } from 'umi-request';
import { queryCurrent, loginVerify } from './services/user';
import defaultSettings from '../config/defaultSettings';
import logo from '@/assets/logo.svg';

export async function getInitialState(): Promise<{
  currentUser?: API.CurrentUser;
  settings?: LayoutSettings;
  isLogin?: boolean;
}> {
  const currentUser = await queryCurrent();
  let user = currentUser.result;
  return {
    currentUser: user,
    settings: defaultSettings,
    isLogin: true,
  };
  return {
    settings: defaultSettings,
  };
}

export const layout = ({
                         initialState,
                       }: {
  initialState: {
    settings?: LayoutSettings;
    currentUser?: API.CurrentUser;
    isLogin: boolean | false;
  };
}): BasicLayoutProps => {
  return {
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    footerRender: () => <Footer />,
    onPageChange: () => {
      // 如果是登录页面，不执行
      if (history.location.pathname !== '/user/login') {
        try {
          // 判断是否登录
          loginVerify().then((res) => {
            if (!res.result) {
              history.push('/user/login');
            }
          });
        } catch (error) {
          history.push('/user/login');
        }
      }
      // 如果没有登录，重定向到 login
      if (!initialState.isLogin && history.location.pathname !== '/user/login') {
        // if (!initialState?.currentUser?.userid && history.location.pathname !== '/user/login') {
        history.push('/user/login');
      }
    },
    menuHeaderRender: undefined,
    ...initialState?.settings,
    logo: logo,
  };
};

const codeMessage = {
  200: '服务器成功返回请求的数据。',
  201: '新建或修改数据成功。',
  202: '一个请求已经进入后台排队（异步任务）。',
  204: '删除数据成功。',
  400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
  401: '用户没有权限（令牌、用户名、密码错误）。',
  403: '用户得到授权，但是访问是被禁止的。',
  404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
  405: '请求方法不被允许。',
  406: '请求的格式不可得。',
  410: '请求的资源被永久删除，且不会再得到的。',
  422: '当创建一个对象时，发生一个验证错误。',
  500: '服务器发生错误，请检查服务器。',
  502: '网关错误。',
  503: '服务不可用，服务器暂时过载或维护。',
  504: '网关超时。',
};

/**
 * 异常处理程序
 */
const errorHandler = (error: ResponseError) => {
  const { response } = error;
  if (response && response.status) {
    const errorText = codeMessage[response.status] || response.statusText;
    const { status, url } = response;

    notification.error({
      message: `请求错误 ${status}: ${url}`,
      description: errorText,
    });
  }

  if (!response) {
    notification.error({
      description: '您的网络发生异常，无法连接服务器',
      message: '网络异常',
    });
  }
  throw error;
};

export const request: RequestConfig = {
  errorHandler,
  errorConfig: {
    adaptor: (resData) => {
      return {
        ...resData,
        errorCode: resData.code,
        errorMessage: resData.msg,
      };
    },
  },
  responseInterceptors: [
    (response) => {
      response
        .clone()
        .json()
        .then((data) => {
          if (
            data.code !== '000' &&
            data.code !== '998' &&
            data.code !== '401' &&
            response.url.indexOf('/v2/api-docs') < 0 &&
            response.url.indexOf('/swagger-resources') < 0 &&
            data.code !== undefined
          ) {
            notification.error({
              message: `请求错误:`,
              description: data.msg,
            });
          }
          if (data.code === '998') {
            history.push('/user/login');
          }
        });
      return response;
    },
  ],
};
