import React, { useCallback, useContext, useEffect, useState } from 'react';
import { Form } from 'antd';
import { useHistory } from 'react-router-dom';
import { InternalNamePath } from 'antd/lib/form/interface';
import { FormListFieldData } from 'antd/lib/form/FormList';
import AuthContext from '../context/AuthContext';
import { Effect } from '../dto/Effect';
import { PagedResult } from '../shared/PagedResult';
import { PAGE_SIZE } from '../configuration/constants';
import axios from '../configuration/axios';
import handleHttpError from '../shared/handleHttpError';
import PagedPickTable from '../shared/PagedPickTable';
import { Card } from '../dto/Card';

type SubjectCardPickerProps = {
  modify: boolean;
  initCards?: Card[];
  field: FormListFieldData;
};

export const SubjectCardPicker: React.FunctionComponent<SubjectCardPickerProps> = ({
  modify = false,
  initCards = [],
  field = null,
}) => {
  const auth = useContext(AuthContext);
  const history = useHistory();

  const [pages, setPages] = useState<PagedResult<Card> | null>(null);
  const [value, setValue] = useState('');

  const changePage = useCallback(
    (page: unknown, filter: string) => {
      axios
        .get<PagedResult<Card>>(
          `/api/subject-card/search?page=${page}&size=${PAGE_SIZE}&query=${encodeURIComponent(
            `subjectCode=ke="${filter}" or subjectName=ke="${filter}"`
          )}`,
          {
            headers: { Authorization: auth.token },
          }
        )
        .then((res) => {
          setPages(res.data);
        })
        .catch((err) => handleHttpError(err, history));
    },
    [auth, history]
  );

  useEffect(() => {
    changePage(0, value);
  }, [changePage, value]);

  return (
    <>
      {pages == null || field == null ? null : (
        <Form.Item
          name={[field?.name, 'subjectCardIds']}
          fieldKey={[field.fieldKey, 'subjectCardIds']}
        >
          <PagedPickTable
            changePage={changePage}
            dataSource={pages.results.map((e) => ({
              ...e,
              value: e.subjectName,
            }))}
            modify={modify}
            initVals={initCards.map((e) => ({
              ...e,
              value: e.subjectName,
            }))}
            onSearch={(f) => setValue(f)}
            columns={[
              {
                title: 'Code',
                dataIndex: 'subjectCode',
              },
              {
                title: 'Name',
                dataIndex: 'subjectName',
              },
            ]}
          />
        </Form.Item>
      )}
    </>
  );
};
