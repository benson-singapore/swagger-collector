import React, { useState, useEffect } from 'react';
import { ApiConfigCascade as Cascade, getApiConfigCascade } from '../ApiConfig';
import { Cascader } from 'antd';

// 定义传参
interface ApiConfigCascadeParams {
  setCascade: (item: Cascade, title: string) => void;
}

/**
 * 新增配置
 *
 * @author zhangby
 * @date 2/9/20 11:54 am
 */
const ApiConfigCascade: React.FC<ApiConfigCascadeParams> = (props) => {
  const { setCascade } = props;
  const [apiConfigArr, setApiConfigArr] = useState<Cascade[]>([]);
  const [value, setValue] = useState<string[]>([]);

  // 初始化
  useEffect(() => {
    // 查询级联
    getApiConfigCascade().then((res) => {
      if (res.code === '000') {
        const result = res?.result;
        // 设置基础数据
        // @ts-ignore
        setApiConfigArr(result);
        // 设置默认选项
        for (let key in result) {
          const item = result[key];
          if (item.children) {
            const first = item.children[0];
            setValue([item.id, first.id]);
            // @ts-ignore
            setCascade(first, `${item.name} ( ${first.name} )`);
            break;
          }
        }
      }
    });
  }, []);

  // 状态变更
  const onChange = (val: string[], record: Cascade[]) => {
    setValue(val);
    setCascade(record[1], `${record[0].name} ( ${record[1].name} )`);
  };

  return (
    <>
      <span>Select a spec</span>
      <Cascader
        allowClear={false}
        options={apiConfigArr}
        fieldNames={{ label: 'name', value: 'id', children: 'children' }}
        style={{ width: 300, marginLeft: 10 }}
        onChange={onChange}
        value={value}
        placeholder="Please select"
      />
    </>
  );
};

export default ApiConfigCascade;
