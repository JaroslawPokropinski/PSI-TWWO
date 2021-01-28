import { Checkbox, Form, Input, InputNumber, Select } from 'antd';
import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { FormattedMessage } from 'react-intl';
import { useHistory, useParams } from 'react-router-dom';
import axios from '../configuration/axios';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import { Effect } from '../dto/Effect';
import EditorView from '../shared/EditorView';
import handleHttpError from '../shared/handleHttpError';
import useQueryParam from '../shared/useQueryParam';
import { VersionHistory } from '../shared/versionHistory';
import VerificationStatus from '../shared/VeryficationStatus';

import './EffectsEditor.css';

const EffectsEditorContent = ({ isArchive = false }) => {
  const lang = useContext(LangContext);

  const { state } = useParams<{ state: string }>();
  const modify = useMemo(
    () => (state === 'create' || state === 'edit') && !isArchive,
    [state, isArchive]
  );
  const [id] = useQueryParam('id');

  return (
    <>
      <VerificationStatus modify={modify} label={lang.getMessage('State')} />
      <Form.Item
        className="effects-form-item"
        label={lang.getMessage('Code')}
        labelAlign="left"
        name="code"
        rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
      >
        <Input disabled={!(state === 'edit' && id === '')} />
      </Form.Item>

      <Form.Item
        className="form-item"
        label={lang.getMessage('Effect type')}
        labelAlign="left"
        name="type"
        rules={[{ required: true, message: 'Wprowadź typ efektu!' }]}
      >
        <Select disabled={!modify}>
          <Select.Option value="MINISTERIAL">
            <FormattedMessage id="Ministerial" />
          </Select.Option>
          <Select.Option value="FIELD_OF_STUDY">
            <FormattedMessage id="Field of study" />
          </Select.Option>
          <Select.Option value="SPECIALISATION">
            <FormattedMessage id="Specialisation" />
          </Select.Option>
          <Select.Option value="SUBJECT">
            <FormattedMessage id="Subject" />
          </Select.Option>
        </Select>
      </Form.Item>

      <Form.Item
        className="effects-form-item"
        label={lang.getMessage('Description')}
        labelAlign="left"
        name="description"
        rules={[{ required: true, message: 'Wprowadź opis efektu!' }]}
      >
        <Input.TextArea disabled={!modify} />
      </Form.Item>

      <Form.Item
        name="category"
        label={lang.getMessage('Category')}
        labelAlign="left"
        hasFeedback
        rules={[{ required: true, message: 'Wybierz kategorie efektu!' }]}
      >
        <Select placeholder="Wybierz kategorie efektu" disabled={!modify}>
          <Select.Option value="KNOWLEDGE">
            <FormattedMessage id="Knowledge" />
          </Select.Option>
          <Select.Option value="SKILLS">
            <FormattedMessage id="Skills" />
          </Select.Option>
          <Select.Option value="SOCIAL_COMPETENCES">
            <FormattedMessage id="Social competences" />
          </Select.Option>
        </Select>
      </Form.Item>

      <Form.Item
        className="effects-form-item"
        label={lang.getMessage('PRK level')}
        labelAlign="left"
        name="prkLevel"
        rules={[{ required: true, message: 'Wprowadź poziom PRK efektu!' }]}
      >
        <InputNumber min={1} max={8} disabled={!modify} />
      </Form.Item>

      <Form.Item
        className="effects-form-item"
        name="isEngineerEffect"
        valuePropName="checked"
      >
        <Checkbox disabled={!modify}>
          <FormattedMessage id="Allows engeener" />
        </Checkbox>
      </Form.Item>

      <Form.Item
        className="effects-form-item"
        name="isLingualEffect"
        valuePropName="checked"
      >
        <Checkbox disabled={!modify}>
          <FormattedMessage id="Lingual" />
        </Checkbox>
      </Form.Item>
      {/* <EffectMappings modify={modify} /> */}
    </>
  );
};

function EffectsEditor(): JSX.Element {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);

  const history = useHistory();

  const [id] = useQueryParam('id');
  const isNew = useMemo(() => id === '', [id]);
  const [effect, setEffect] = useState<Effect | null>(null);

  const onFinish = useCallback(
    (d) => {
      const results = {
        ...d,
        isEngineerEffect: d.isEngineerEffect ?? false,
        isLingualEffect: d.isLingualEffect ?? false,
      };

      if (isNew) {
        axios
          .post(`/api/educational-effects`, [results], {
            headers: { Authorization: auth.token },
          })
          .then((res) => {
            setEffect(res.data[0]);
            history.goBack();
          })
          .catch((err) => handleHttpError(err, history));
        return;
      }
      if (effect == null) return;

      axios
        .put(`/api/educational-effects`, [{ ...effect, ...results }], {
          headers: { Authorization: auth.token },
        })
        .then((res) => {
          setEffect(res.data[0]);
          history.goBack();
        })
        .catch((err) => handleHttpError(err, history));
    },
    [isNew, effect, history, auth]
  );

  const onRemove = useCallback(() => {
    if (effect?.id == null) return;

    axios
      .delete(`/api/educational-effects/${effect.id}`, {
        headers: { Authorization: auth.token },
      })
      .then(() => {})
      .catch((err) => handleHttpError(err, history));
  }, [effect, history, auth]);

  useEffect(() => {
    if (isNew) return;

    axios
      .get<Effect[]>(`/api/educational-effects/${id}`, {
        headers: { Authorization: auth.token },
      })
      .then((res) => {
        setEffect(res.data[0]);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, auth]);

  const [archiveVals, setArchiveVals] = useState<VersionHistory<Effect> | null>(
    null
  );

  useEffect(() => {
    if (isNew) return;

    const versionHistory = new VersionHistory<Effect>(
      '/api/educational-effects/history',
      id,
      { headers: { Authorization: auth.token } }
    );
    versionHistory
      .init()
      .then(() => {
        setArchiveVals(versionHistory);
      })
      .catch((e) => handleHttpError(e, history));
  }, [isNew, id, auth, history]);

  return (
    <div className="effects-editor">
      {effect == null && !isNew ? null : (
        <EditorView
          header={lang.getMessage('Studies effect')}
          initialVals={effect ?? {}}
          useArchive
          isVerifiable={false}
          isAllowedToEdit={['ROLE_ADMIN', 'ROLE_COMMISSION_MEMBER'].includes(
            auth.role
          )}
          versionHistory={archiveVals}
          name="effects"
          onFinish={onFinish}
          queryParams=""
          onRemove={onRemove}
        >
          <EffectsEditorContent />
          <EffectsEditorContent isArchive />
        </EditorView>
      )}
    </div>
  );
}

export default EffectsEditor;
