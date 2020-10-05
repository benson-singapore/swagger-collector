import React, { useState } from 'react';
import { Button, Drawer, Spin, message, Input, notification } from 'antd';
import { PlusOutlined, VerticalAlignBottomOutlined } from '@ant-design/icons';
import ProForm, { ProFormText, ProFormSelect, ProFormDigit } from '@ant-design/pro-form';
import { getMethodDictList, addAutoAuth, getAuthorization, getMaxSort } from '../AutoAuth';

// 定义传参
interface AutoAuthAddParams {
  refresh: () => void;
}

/**
 * 新增自动授权数据
 *
 * @author zhangby
 * @date 14/9/20 4:15 pm
 */
const AutoAuthAdd: React.FC<AutoAuthAddParams> = (props) => {
  const { refresh } = props;
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [token, setToken] = useState<string>('');

  const [form] = ProForm.useForm();

  // 初始化
  const onPreInit = async () => {
    setVisible(true);
    form.resetFields();
    setLoading(true);
    // 查询最大排序值
    try {
      const result = await getMaxSort();
      console.log(result);
      form.setFieldsValue({
        sort: result.result,
        tokenKey: 'result.tails.access_token',
      });
      setLoading(false);
    } catch (e) {
      setLoading(false);
    }
  };

  // 提交
  const onSubmit = () => {
    form.validateFields().then(async (values) => {
      setLoading(true);
      await addAutoAuth(values);
      message.success('新建成功！');
      setLoading(false);
      setVisible(false);
      refresh();
    });
  };

  // 调用授权
  const authorization = async () => {
    const record = form.getFieldsValue();
    try {
      const result = await getAuthorization(record);
      // 格式化数据
      if (record.tokenKey) {
        let res = result;
        record.tokenKey.split('.').forEach((key) => {
          res = res[key];
        });
        setToken(res);
      }
    } catch (e) {
      notification['error']({
        message: '访问失败',
        description: '请求授权失败，请稍后再试！',
      });
      console.log(e);
      setToken('undefined');
    }
  };

  return (
    <>
      <Button key="add" type="primary" onClick={onPreInit}>
        <PlusOutlined />
        新建
      </Button>
      {/* 弹出窗口 */}
      <Drawer
        title="新建自动授权"
        placement="right"
        closable={false}
        onClose={() => setVisible(false)}
        visible={visible}
        width={650}
        footer={
          <div style={{ textAlign: 'right' }}>
            <Button onClick={() => setVisible(false)} style={{ marginRight: 8 }}>
              取消
            </Button>
            <Button onClick={onSubmit} type="primary">
              确定
            </Button>
          </div>
        }
      >
        <Spin spinning={loading}>
          <ProForm submitter={false} form={form}>
            <ProForm.Group>
              <ProFormText
                style={{ width: 300 }}
                name="name"
                label="名称"
                placeholder="请输入名称"
                rules={[{ required: true, message: '请输入名称' }]}
              />
              <ProFormSelect
                name="method"
                label="请求方式"
                hasFeedback
                style={{ width: 270 }}
                request={async () => await getMethodDictList()}
                placeholder="选择请求方式"
                rules={[{ required: true, message: '请选择请求方式' }]}
              />
            </ProForm.Group>
            <ProFormText
              name="url"
              label="访问地址"
              placeholder="请输入访问地址"
              rules={[{ required: true, message: '请输入访问地址' }]}
            />
            <ProForm.Group>
              <ProFormText
                style={{ width: 300 }}
                name="username"
                label="用户名"
                placeholder="请输入用户名"
                rules={[{ required: true, message: '请输入用户名' }]}
              />
              <ProFormText
                style={{ width: 270 }}
                name="password"
                label="密码"
                placeholder="请输入密码"
                rules={[{ required: true, message: '请输入密码' }]}
              />
            </ProForm.Group>
            <ProFormDigit
              style={{ width: 200 }}
              label="排序"
              name="sort"
              min={0}
              step={1}
              precision={0}
            />
            <ProFormText
              tip="手动指定数据token获取键值"
              name="tokenKey"
              label="Token Key"
              placeholder=""
            />
          </ProForm>
          <h3>测试授权</h3>
          <div style={{ textAlign: 'center', marginBottom: 20 }}>
            <Button type="primary" onClick={authorization}>
              <VerticalAlignBottomOutlined />
              快速授权
            </Button>
          </div>
          <div style={{ marginBottom: 5, fontSize: 15 }}>Authorization</div>
          <Input.TextArea
            autoSize={{ minRows: 2, maxRows: 6 }}
            placeholder="Token 授权码"
            value={token}
          />
        </Spin>
      </Drawer>
    </>
  );
};

export default AutoAuthAdd;
