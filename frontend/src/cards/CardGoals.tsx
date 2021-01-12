import React, { useContext } from 'react';
import { Button, Card, Divider, Form, Input } from 'antd';
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
              <>
                <Form.Item
                  label={lang.getMessage('Subject objective')}
                  labelAlign="left"
                  name={field.name}
                  fieldKey={field.fieldKey}
                  key={field.key}
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
              </>
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
