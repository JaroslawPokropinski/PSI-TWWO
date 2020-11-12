import { Modal } from 'antd';
import React from 'react';

function VerifyModal(props: {
  visible: boolean;
  onOk: () => void;
  onCancel: () => void;
}): JSX.Element {
  const { visible, onOk, onCancel } = props;
  return (
    <Modal
      title="Jesteś pewien?"
      visible={visible}
      onOk={onOk}
      onCancel={onCancel}
    >
      <p>Czy na pewno chcesz oznaczyć obiekt jako zweryfikowany?</p>
      <p>Kliknij OK aby potwierdźić.</p>
    </Modal>
  );
}

export default VerifyModal;
