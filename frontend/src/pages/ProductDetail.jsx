import { Row, Col, Typography, Tag, Button, Spin, Divider } from 'antd'
import { ArrowLeftOutlined } from '@ant-design/icons'
import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getProduct } from '../api/productApi'

const { Title, Text, Paragraph } = Typography

export default function ProductDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [product, setProduct] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getProduct(id)
      .then(({ data }) => setProduct(data))
      .catch(() => navigate('/'))
      .finally(() => setLoading(false))
  }, [id, navigate])

  if (loading) {
    return <div style={{ textAlign: 'center', padding: 80 }}><Spin size="large" /></div>
  }
  if (!product) return null

  return (
    <div style={{ padding: '24px', maxWidth: 900, margin: '0 auto' }}>
      <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/')} style={{ marginBottom: 24 }}>
        Back to Catalog
      </Button>
      <Row gutter={[40, 24]}>
        <Col xs={24} md={10}>
          <img
            src={product.imageUrl}
            alt={product.title}
            style={{ width: '100%', maxHeight: 400, objectFit: 'contain', background: '#fafafa', padding: 24, borderRadius: 8 }}
          />
        </Col>
        <Col xs={24} md={14}>
          <Title level={2}>{product.title}</Title>
          {product.category?.title && (
            <Tag color="blue" style={{ marginBottom: 16 }}>{product.category.title}</Tag>
          )}
          <Title level={3} type="success" style={{ margin: '8px 0 16px' }}>
            ${product.price?.toFixed(2)}
          </Title>
          <Divider />
          <Text strong>Description</Text>
          <Paragraph style={{ marginTop: 8, color: '#555' }}>{product.description}</Paragraph>
        </Col>
      </Row>
    </div>
  )
}
