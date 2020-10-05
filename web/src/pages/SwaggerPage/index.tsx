import React, { useRef, useState, useEffect } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import SwaggerUI from 'swagger-ui-react';
import 'swagger-ui-react/swagger-ui.css';
import { Card, Button, Tooltip, Spin, message, Modal, Form, Input } from 'antd';
import ApiConfigCascade from '@/pages/ApiConfigList/components/ApiConfigCascade';
import { ApiConfig } from '@/pages/ApiConfigList/ApiConfig';
import { AutoAuth, getAutoAuthById, getToken } from '@/pages/AutoAuthList/AutoAuth';
import moment from 'moment';
import {
  SyncOutlined,
  SafetyCertificateOutlined,
  LogoutOutlined,
  ApiOutlined,
} from '@ant-design/icons';

/**
 * SPagger 文档页面
 */
export default (): React.ReactNode => {
  const [cascade, setCascade] = useState<ApiConfig>({});
  const [autoAuth, setAutoAuth] = useState<AutoAuth>({});
  const [title, setTitle] = useState<string>('');
  const [key, setKey] = useState<number>('key');
  const [token, setToken] = useState<string>('');
  const [loading, setLoading] = useState(false);
  const [visible, setVisible] = useState(false);

  const [form] = Form.useForm();

  useEffect(() => {
    changeToken(localStorage.getItem('token'));
  }, []);

  const refreshSwagger = () => {
    setKey(new Date().getTime());
  };

  // 更新token
  const changeToken = (token: string) => {
    setToken(token);
    form.setFieldsValue({ token: token });
    refreshSwagger();
  };

  const changeSwagger = async (record: ApiConfig, title: string) => {
    setLoading(true);
    setCascade(record);
    setTitle(title);
    setAutoAuth({});
    refreshSwagger();
    // 查询自动授权
    if (record.autoAuthId) {
      try {
        // 设置token
        changeToken(localStorage.getItem('token'));
        // 获取授权配置
        const autoAuthResult = await getAutoAuthById(record.autoAuthId);
        if (autoAuthResult) {
          setAutoAuth(autoAuthResult.result);
        }
        setLoading(false);
      } catch (e) {
        setLoading(false);
      }
    } else {
      setLoading(false);
    }
  };

  // 快速授权
  const fastAuth = async () => {
    try {
      setLoading(true);
      const token = await getToken(autoAuth);
      if (token) {
        message.success('授权成功！');
        changeToken(token);
        localStorage.setItem('token', token);
      } else {
        message.error('授权失败，请检查授权配置');
      }
      setLoading(false);
    } catch (e) {
      setLoading(false);
    }
  };

  // 清空授权
  const clearAuth = async () => {
    try {
      setLoading(true);
      message.success('清空授权成功！');
      changeToken('');
      localStorage.setItem('token', '');
      setLoading(false);
    } catch (e) {
      setLoading(false);
    }
  };

  // 提交
  const onSubmit = () => {
    form.validateFields().then((values) => {
      changeToken(values.token);
      localStorage.setItem('token', values.token);
      setVisible(false);
    });
  };

  return (
    <>
      <PageContainer
        title={
          <span>
            <span>Swagger UI</span>
            <span style={{ fontSize: 14, fontWeight: 400, marginLeft: 10 }}>{title}</span>
            <span>
              <Tooltip placement="bottom" title={'edit token'}>
                <Button type="link" style={{ color: 'black' }} onClick={() => setVisible(true)}>
                  <SafetyCertificateOutlined style={{ fontSize: 14 }} />
                </Button>
              </Tooltip>
              <Tooltip placement="bottom" title={'refresh swagger'}>
                <Button
                  type="link"
                  style={{ color: 'black', paddingLeft: 0 }}
                  onClick={refreshSwagger}
                >
                  <SyncOutlined style={{ fontSize: 14 }} />
                </Button>
              </Tooltip>
            </span>
          </span>
        }
        extra={[<ApiConfigCascade key="1" setCascade={changeSwagger} />]}
      >
        <Spin spinning={loading}>
          <Card style={{ background: '#fafafa' }}>
            <div>
              {autoAuth.url ? (
                <div>
                  <div style={{ textAlign: 'right', marginBottom: 20 }}>
                    <Button type="link" style={{ color: '#414141' }} onClick={fastAuth}>
                      <SafetyCertificateOutlined /> 快速授权
                    </Button>
                    <Button type="link" style={{ color: '#414141' }} onClick={clearAuth}>
                      <LogoutOutlined /> 清空授权
                    </Button>
                  </div>
                  <SwaggerUI
                    key={key + token + cascade.value}
                    url={cascade.host + cascade.value}
                    docExpansion={'none'}
                    onComplete={(swaggerUi: any) => {
                      swaggerUi.preauthorizeApiKey('Authorization', token);
                    }}
                    requestInterceptor={(req) => {
                      req.headers = {
                        Authorization: token,
                      };
                      return req;
                    }}
                  />
                </div>
              ) : (
                <div>
                  <SwaggerUI
                    key={key + token}
                    url={cascade.host + cascade.value}
                    docExpansion={'none'}
                  />
                </div>
              )}
            </div>
          </Card>
        </Spin>

        <Modal
          title="编辑Token"
          visible={visible}
          onOk={onSubmit}
          onCancel={() => setVisible(false)}
          width={620}
        >
          <Form {...layout} form={form}>
            <Form.Item name="token" label="Authorization">
              <Input.TextArea placeholder="token" autoSize={{ minRows: 3, maxRows: 5 }} />
            </Form.Item>
          </Form>
        </Modal>
      </PageContainer>
    </>
  );
};

const layout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 7 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 12 },
  },
};
