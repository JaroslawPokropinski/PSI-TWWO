import React from 'react';
import { Button, Card, Divider, Form, Input, InputNumber } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { InternalNamePath } from 'antd/lib/form/interface';

const ProgramContent: React.FunctionComponent<{
  modify: boolean;
  name: string | number | InternalNamePath | undefined;
}> = ({ modify = false, name = '' }) => {
  return (
    <Card title="Treśći programowe">
      <Form.List name={name}>
        {(fields, { add, remove }) => (
          <>
            {fields.map((field) => (
              <div key={field.key}>
                <Form.Item
                  label="Opis"
                  labelAlign="left"
                  name={[field.name, 'description']}
                  fieldKey={[field.fieldKey, 'description']}
                  rules={[
                    { required: true, message: 'Podaj treść programową!' },
                  ]}
                >
                  <Input
                    placeholder="Podaj treść programową"
                    disabled={!modify}
                  />
                </Form.Item>

                <Form.Item
                  label="godziny"
                  labelAlign="left"
                  name={[field.name, 'numberOfHours']}
                  fieldKey={[field.fieldKey, 'numberOfHours']}
                  rules={[
                    {
                      required: true,
                      message:
                        'Podaj godziny przeznaczone na treść programową!',
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
    </Card>
  );
};

export default ProgramContent;
