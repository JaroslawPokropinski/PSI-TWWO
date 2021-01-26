import React, { useContext } from 'react';
import { Button, Card, Divider, Form, Input, Space } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { FormattedMessage } from 'react-intl';
import { LangContext } from '../context/LangContext';

const CardGoals: React.FunctionComponent<{ modify: boolean }> = ({
  modify = false,
}) => {
  const lang = useContext(LangContext);

  return (
    <Card title={lang.getMessage('Subject objective')}>
      <Form.List name="subjectObjectives">
        {(fields, { add, remove }) => (
          <>
            {fields.map((field) => (
              <Space key={field.key}>
                <Form.Item
                  label={lang.getMessage('Subject objective')}
                  labelAlign="left"
                  name={field.name}
                  fieldKey={field.fieldKey}
                  rules={[{ required: true, message: 'Podaj cel!' }]}
                >
                  <Input
                    placeholder={lang.getMessage('Fill subject objective')}
                    disabled={!modify}
                  />
                </Form.Item>

                {modify ? (
                  <Button
                    type="dashed"
                    onClick={() => remove(field.name)}
                    icon={<MinusCircleOutlined />}
                  >
                    Usuń opis wyżej
                  </Button>
                ) : null}
                <Divider />
              </Space>
            ))}
            {modify ? (
              <Form.Item>
                <Button
                  type="dashed"
                  onClick={() => add()}
                  block
                  icon={<PlusOutlined />}
                >
                  <FormattedMessage id="Add subject objective" />
                </Button>
              </Form.Item>
            ) : null}
          </>
        )}
      </Form.List>
    </Card>
  );
};

export default CardGoals;
