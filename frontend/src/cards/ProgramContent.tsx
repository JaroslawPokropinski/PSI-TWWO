import React from 'react';
import { Button, Divider, Form, Input, InputNumber, Space } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { InternalNamePath } from 'antd/lib/form/interface';

const ProgramContent: React.FunctionComponent<{
  modify: boolean;
  name: string | number | InternalNamePath | undefined;
}> = ({ modify = false, name = '' }) => {
  return (
    <Form.List name={name}>
      {(fields, { add, remove }) => (
        <>
          {fields.map((field) => (
            <div key={field.key}>
              <Form.Item
                label="Opis"
                labelAlign="left"
                name={[field.name, 'content']}
                rules={[{ required: true, message: 'Podaj treść programową!' }]}
              >
                <Input
                  placeholder="Podaj treść programową"
                  disabled={!modify}
                />
              </Form.Item>

              <Form.Item
                label="godziny"
                labelAlign="left"
                name={[field.name, 'hours']}
                rules={[
                  {
                    required: true,
                    message: 'Podaj godziny przeznaczone na treść programową!',
                  },
                ]}
              >
                <InputNumber min={1} max={900} disabled={!modify} />
              </Form.Item>

              {modify ? (
                <Button
                  type="dashed"
                  onClick={() => remove(field.name)}
                  icon={<MinusCircleOutlined />}
                >
                  Usuń treść programową
                </Button>
              ) : null}
              <Divider />
            </div>
          ))}
          {modify ? (
            <Form.Item>
              <Button
                type="dashed"
                onClick={() => add()}
                block
                icon={<PlusOutlined />}
              >
                Dodaj treść programową
              </Button>
            </Form.Item>
          ) : null}
        </>
      )}
    </Form.List>
  );
};

export default ProgramContent;
