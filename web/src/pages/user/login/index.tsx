import { AlipayCircleOutlined, TaobaoCircleOutlined, WeiboCircleOutlined } from '@ant-design/icons';
import { Alert, Checkbox, message } from 'antd';
import React, { useState } from 'react';
import { Link, SelectLang, useModel } from 'umi';
import { getPageQuery } from '@/utils/utils';
import logo from '@/assets/swagger_logo.png';
import { LoginParamsType, fakeAccountLogin } from '@/services/login';
import Footer from '@/components/Footer';
import LoginFrom from './components/Login';
import styles from './style.less';

const { Tab, Username, Password, Mobile, Captcha, Submit } = LoginFrom;

const LoginMessage: React.FC<{
  content: string;
}> = ({ content }) => (
  <Alert
    style={{
      marginBottom: 24,
    }}
    message={content}
    type="error"
    showIcon
  />
);

/**
 * 此方法会跳转到 redirect 参数所在的位置
 */
const replaceGoto = () => {
  const urlParams = new URL(window.location.href);
  const params = getPageQuery();
  let { redirect } = params as { redirect: string };
  if (redirect) {
    const redirectUrlParams = new URL(redirect);
    if (redirectUrlParams.origin === urlParams.origin) {
      redirect = redirect.substr(urlParams.origin.length);
      if (redirect.match(/^\/.*#/)) {
        redirect = redirect.substr(redirect.indexOf('#'));
      }
    } else {
      window.location.href = '/';
      return;
    }
  }
  window.location.href = urlParams.href.split(urlParams.pathname)[0] + (redirect || '/');
};

const Login: React.FC<{}> = () => {
  const [userLoginState, setUserLoginState] = useState<API.LoginStateType>({});
  const [submitting, setSubmitting] = useState(false);

  const { refresh } = useModel('@@initialState');
  const [autoLogin, setAutoLogin] = useState(true);
  const [type, setType] = useState<string>('account');

  const handleSubmit = async (values: LoginParamsType) => {
    setSubmitting(true);
    try {
      // 登录
      const msg = await fakeAccountLogin({ ...values, type });
      if (msg.code === '000') {
        message.success('登录成功！');
        replaceGoto();
        setTimeout(() => {
          refresh();
        }, 0);
        return;
      }
      // 如果失败去设置用户错误信息
      setUserLoginState({ status: 'error', type: msg.msg });
    } catch (error) {
      // message.error('登录失败，请重试！');
    }
    setSubmitting(false);
  };

  const { status, type: loginType } = userLoginState;

  return (
    <div className={styles.container}>
      {/*<div className={styles.lang}>*/}
      {/*  <SelectLang />*/}
      {/*</div>*/}
      <div className={styles.content}>
        <div className={styles.top} style={{ marginTop: '13vh' }}>
          <div className={styles.header}>
            <Link to="/">
              <img alt="logo" className={styles.logo} style={{ height: 54 }} src={logo} />
              <span className={styles.title} style={{ color: '#1d3b47' }}>
                System Login
              </span>
            </Link>
          </div>
          <div className={styles.desc} style={{ marginTop: 20 }}>
            Swagger-UI 登录管理系统
          </div>
        </div>

        <div className={styles.main}>
          <LoginFrom activeKey={type} onTabChange={setType} onSubmit={handleSubmit}>
            <div
              style={{
                textAlign: 'center',
                fontSize: 16,
                color: '#1990ff',
                fontWeight: 600,
                margin: 20,
              }}
            >
              账户密码登录
            </div>
            {/*<Tab key="account" tab="账户密码登录">*/}
            {status === 'error' && loginType === 'account' && !submitting && (
              <LoginMessage content="账户或密码错误（admin/ant.design）" />
            )}

            <Username
              name="username"
              placeholder="用户名: admin or user"
              rules={[
                {
                  required: true,
                  message: '请输入用户名!',
                },
              ]}
            />
            <Password
              name="password"
              placeholder="密码: ant.design"
              rules={[
                {
                  required: true,
                  message: '请输入密码！',
                },
              ]}
            />
            {/*</Tab>*/}
            <div>
              <Checkbox checked={autoLogin} onChange={(e) => setAutoLogin(e.target.checked)}>
                自动登录
              </Checkbox>
              <a
                style={{
                  float: 'right',
                }}
              >
                忘记密码
              </a>
            </div>
            <Submit loading={submitting}>登录</Submit>
          </LoginFrom>
        </div>
      </div>
      {/*<Footer />*/}
    </div>
  );
};

export default Login;
