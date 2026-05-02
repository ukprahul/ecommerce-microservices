import { Form, Input, InputNumber, Button, Modal } from 'antd'
import { useEffect } from 'react'

export default function ProductForm({ open, onClose, onSubmit, initialValues, loading }) {
  const [form] = Form.useForm()

  useEffect(() => {
    if (open) {
      form.setFieldsValue(initialValues || { title: '', price: 0, description: '', image: '', category: '' })
    }
  }, [open, initialValues, form])

  const handleFinish = (values) => {
    onSubmit(values)
  }

  return (
    <Modal
      title={initialValues ? 'Edit Product' : 'Create Product'}
      open={open}
      onCancel={onClose}
      footer={null}
      destroyOnClose
    >
      <Form form={form} layout="vertical" onFinish={handleFinish} style={{ marginTop: 16 }}>
        <Form.Item name="title" label="Title" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="price" label="Price" rules={[{ required: true }]}>
          <InputNumber min={0} step={0.01} style={{ width: '100%' }} prefix="$" />
        </Form.Item>
        <Form.Item name="description" label="Description" rules={[{ required: true }]}>
          <Input.TextArea rows={3} />
        </Form.Item>
        <Form.Item name="image" label="Image URL" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="category" label="Category" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item style={{ marginBottom: 0, textAlign: 'right' }}>
          <Button onClick={onClose} style={{ marginRight: 8 }}>Cancel</Button>
          <Button type="primary" htmlType="submit" loading={loading}>
            {initialValues ? 'Update' : 'Create'}
          </Button>
        </Form.Item>
      </Form>
    </Modal>
  )
}
