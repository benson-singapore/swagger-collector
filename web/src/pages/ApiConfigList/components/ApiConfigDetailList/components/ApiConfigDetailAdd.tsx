import React, { useState } from 'react';
import { Button, Modal, Form, Input, InputNumber, message, Spin, Select } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { AddApiConfig, getApiConfigMaxSort } from '../../../ApiConfig';
import { getSelectData } from '@/pages/AutoAuthList/AutoAuth';

const { Option } = Select;

// 定义传参
interface ApiConfigDetailAddParams {
  refresh: () => void;
  parentId: string;
  parentName: string;
}

/**
 * 新增配置
 *
 * @author zhangby
 * @date 2/9/20 11:54 am
 */
const ApiConfigDetailAdd: React.FC<ApiConfigDetailAddParams> = (props) => {
  const { parentId, parentName } = props;
  const [visible, setVisible] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);
  const [selectData, setSelectData] = useState<API.Dict[]>([]);

  const [form] = Form.useForm();

  // 初始化
  const onPreInit = async () => {
    form.resetFields();
    setVisible(true);
    // 获取最大排序值
    try {
      setLoading(true);
      const maxSort = await getApiConfigMaxSort(parentId);
      setLoading(false);
      form.setFieldsValue({ sort: maxSort.result });
      // 选择下拉框
      const selectData = await getSelectData();
      if (selectData) {
        setSelectData(selectData.result);
      }
    } catch (e) {
      setLoading(false);
    }
  };

  // 提交
  const onSubmit = () => {
    form.validateFields().then(async (values) => {
      setLoading(true);
      //新增
      await AddApiConfig({ ...values, parentId: parentId });
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
            <Form.Item label="父类接口">
              <Input disabled value={parentName} />
            </Form.Item>
            <Form.Item
              label="接口名称"
              name="name"
              rules={[{ required: true, message: '请输入接口名称!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item
              label="服务地址"
              name="host"
              rules={[{ required: true, message: '请输入服务地址!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item
              label="接口地址"
              name="value"
              rules={[{ required: true, message: '请输入接口地址!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item label="Token 授权" name="autoAuthId">
              <Select allowClear={true}>
                {selectData.map((item) => (
                  <Option value={item.value} key={item.value}>
                    {item.label}
                  </Option>
                ))}
              </Select>
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

export default ApiConfigDetailAdd;
