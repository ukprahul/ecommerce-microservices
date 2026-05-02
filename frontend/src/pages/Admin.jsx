import { Table, Button, Space, Popconfirm, message, Typography, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import { useState, useEffect, useCallback } from 'react'
import { getAllProducts, createProduct, updateProduct, deleteProduct } from '../api/productApi'
import { useAuth } from '../context/AuthContext'
import ProductForm from '../components/ProductForm'

const { Title } = Typography

export default function Admin() {
  const { token } = useAuth()
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState(null)
  const [submitting, setSubmitting] = useState(false)

  const fetchProducts = useCallback(() => {
    setLoading(true)
    getAllProducts()
      .then(({ data }) => setProducts(data))
      .catch(() => message.error('Failed to load products'))
      .finally(() => setLoading(false))
  }, [])

  useEffect(() => { fetchProducts() }, [fetchProducts])

  const handleCreate = () => {
    setEditing(null)
    setFormOpen(true)
  }

  const handleEdit = (product) => {
    setEditing({
      id: product.id,
      title: product.title,
      price: product.price,
      description: product.description,
      image: product.imageUrl,
      category: product.category?.title || '',
    })
    setFormOpen(true)
  }

  const handleDelete = async (id) => {
    try {
      await deleteProduct(id, token)
      message.success('Product deleted')
      fetchProducts()
    } catch {
      message.error('Failed to delete product')
    }
  }

  const handleSubmit = async (values) => {
    setSubmitting(true)
    try {
      if (editing) {
        await updateProduct(editing.id, values, token)
        message.success('Product updated')
      } else {
        await createProduct(values, token)
        message.success('Product created')
      }
      setFormOpen(false)
      fetchProducts()
    } catch {
      message.error('Failed to save product')
    } finally {
      setSubmitting(false)
    }
  }

  const columns = [
    {
      title: 'Image',
      dataIndex: 'imageUrl',
      width: 80,
      render: (url) => (
        <img src={url} alt="product" style={{ width: 50, height: 50, objectFit: 'contain' }} />
      ),
    },
    {
      title: 'Title',
      dataIndex: 'title',
      ellipsis: true,
    },
    {
      title: 'Price',
      dataIndex: 'price',
      width: 100,
      render: (p) => `$${p?.toFixed(2)}`,
    },
    {
      title: 'Category',
      dataIndex: ['category', 'title'],
      width: 140,
      render: (cat) => cat ? <Tag color="blue">{cat}</Tag> : '-',
    },
    {
      title: 'Actions',
      width: 140,
      render: (_, record) => (
        <Space>
          <Button
            icon={<EditOutlined />}
            size="small"
            onClick={() => handleEdit(record)}
          />
          <Popconfirm
            title="Delete this product?"
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <Button icon={<DeleteOutlined />} size="small" danger />
          </Popconfirm>
        </Space>
      ),
    },
  ]

  return (
    <div style={{ padding: '24px' }}>
      <Space style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Title level={3} style={{ margin: 0 }}>Admin — Products</Title>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleCreate}>
          Add Product
        </Button>
      </Space>

      <Table
        columns={columns}
        dataSource={products}
        rowKey="id"
        loading={loading}
        pagination={{ pageSize: 10 }}
      />

      <ProductForm
        open={formOpen}
        onClose={() => setFormOpen(false)}
        onSubmit={handleSubmit}
        initialValues={editing}
        loading={submitting}
      />
    </div>
  )
}
