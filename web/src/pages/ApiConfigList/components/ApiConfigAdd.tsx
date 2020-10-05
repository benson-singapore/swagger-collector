import React, { useState } from 'react';
import { Button, Modal, Form, Input, InputNumber, message, Spin } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { AddApiConfig, getApiConfigMaxSort } from '../ApiConfig';

// 定义传参
interface Params {
  refresh: () => void;
}

/**
 * 新增配置
 *
 * @author zhangby
 * @date 2/9/20 11:54 am
 */
const ApiConfigAdd: React.FC<Params> = (props) => {
  const [visible, setVisible] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);

  const [form] = Form.useForm();

  // 初始化
  const onPreInit = async () => {
    form.resetFields();
    setVisible(true);
    // 获取最大排序值
    try {
      setLoading(true);
      const maxSort = await getApiConfigMaxSort();
      setLoading(false);
      form.setFieldsValue({ sort: maxSort.result });
    } catch (e) {
      setLoading(false);
    }
  };

  // 提交
  const onSubmit = () => {
    form.validateFields().then(async (values) => {
      setLoading(true);
      //新增
      await AddApiConfig(values);
      message.success('新建成功！');
      setLoading(false);
      setVisible(false);
      props.refresh();
    });
  };

  return (
    <span>
      <Button key="add" type="primary" onClick={onPreInit}>
        <PlusOutlined />
        新建
      </Button>
      <Modal
        title="新建接口配置"
        visible={visible}
        onOk={onSubmit}
        onCancel={() => setVisible(false)}
        width={550}
      >
        <Spin spinning={loading}>
          <Form {...layout} form={form}>
            <Form.Item
              label="接口分组"
              name="name"
              rules={[{ required: true, message: '请输入接口分组!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item
              label="分组类型"
              name="value"
              rules={[{ required: true, message: '请输入分组类型!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item label="排序" name="sort">
              <InputNumber min={0} step={1} precision={0} defaultValue={10} />
            </Form.Item>
            <Form.Item label="备注" name="remarks">
              <Input.TextArea rows={3} placeholder="备注信息" />
            </Form.Item>
          </Form>
        </Spin>
      </Modal>
    </span>
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

export default ApiConfigAdd;
