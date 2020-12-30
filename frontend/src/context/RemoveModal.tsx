import { Modal } from 'antd';
import React from 'react';

function RemoveModal(props: {
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
      <p>Usunięcie obiektu jest akcją nieodwracalną!</p>
      <p>Kliknij OK aby potwierdźić.</p>
    </Modal>
  );
}

export default RemoveModal;
