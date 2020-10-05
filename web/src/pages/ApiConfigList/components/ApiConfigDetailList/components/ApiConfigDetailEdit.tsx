import React, { useState } from 'react';
import { Spin, Button, Modal, Form, Input, InputNumber, message, Radio, Select } from 'antd';
import { EditOutlined } from '@ant-design/icons';
import { updateApiConfig, getApiConfigById } from '@/pages/ApiConfigList/ApiConfig';
import { getSelectData } from '@/pages/AutoAuthList/AutoAuth';

const { Option } = Select;

// 参数
interface ApiConfigEditParams {
  refresh: () => void;
  apiConfigId: string;
  parentName: string;
}

/**
 * 编辑接口文档
 * @param props
 * @constructor
 */
const ApiConfigDetailEdit: React.FC<ApiConfigEditParams> = (props) => {
  const { apiConfigId, refresh, parentName } = props;
  const [loading, setLoading] = useState<boolean>(false);
  const [visible, setVisible] = useState<boolean>(false);
  const [selectData, setSelectData] = useState<API.Dict[]>([]);

  const [form] = Form.useForm();

  //初始化
  const onPreInit = async () => {
    setVisible(true);
    // 查询数据
    try {
      setLoading(true);
      const result = await getApiConfigById(apiConfigId);
      setLoading(false);
      form.setFieldsValue({ ...result?.result });
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
      //更新
      await updateApiConfig(apiConfigId, values);
      message.success('更新成功！');
      setLoading(false);
      setVisible(false);
      refresh();
    });
  };

  return (
    <span>
      <Button type="primary" shape="circle" onClick={onPreInit}>
        <EditOutlined />
      </Button>

      <Modal
        title="编辑接口配置"
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
              rules={[{ required: true, message: '请输入接口分组!' }]}
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
            <Form.Item label="状态" name="status">
              <Radio.Group>
                <Radio value={'0'}>正常</Radio>
                <Radio value={'1'}>停用</Radio>
              </Radio.Group>
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

export default ApiConfigDetailEdit;
