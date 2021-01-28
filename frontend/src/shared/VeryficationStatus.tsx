import { Form, Select } from 'antd';
import React from 'react';
import { FormattedMessage } from 'react-intl';

const VerificationStatus = ({
  modify = false,
  label = 'State',
}: {
  modify: boolean;
  label: string;
}): JSX.Element => {
  return (
    <>
      {modify ? null : (
        <Form.Item
          className="effects-form-item"
          label={label}
          labelAlign="left"
          name="objectState"
        >
          <Select disabled>
            <Select.Option value="ACTIVE">
              <FormattedMessage id="Active" />
            </Select.Option>
            <Select.Option value="VERIFIED">
              <FormattedMessage id="Verified" />
            </Select.Option>
            <Select.Option value="UNVERIFIED">
              <FormattedMessage id="Unverified" />
            </Select.Option>
            <Select.Option value="INACTIVE">
              <FormattedMessage id="Inactive" />
            </Select.Option>
          </Select>
        </Form.Item>
      )}
    </>
  );
};

export default VerificationStatus;
