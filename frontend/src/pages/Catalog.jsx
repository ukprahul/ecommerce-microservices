import { Row, Col, Select, Spin, Empty, Typography, Space } from 'antd'
import { useState, useEffect } from 'react'
import { getAllProducts, getCategories, getProductsByCategory } from '../api/productApi'
import ProductCard from '../components/ProductCard'

const { Title } = Typography

export default function Catalog() {
  const [products, setProducts] = useState([])
  const [categories, setCategories] = useState([])
  const [selectedCategory, setSelectedCategory] = useState('all')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getCategories()
      .then(({ data }) => setCategories(data))
      .catch(() => {})
  }, [])

  useEffect(() => {
    setLoading(true)
    const fetch = selectedCategory === 'all'
      ? getAllProducts()
      : getProductsByCategory(selectedCategory)

    fetch
      .then(({ data }) => setProducts(data))
      .catch(() => setProducts([]))
      .finally(() => setLoading(false))
  }, [selectedCategory])

  return (
    <div style={{ padding: '24px' }}>
      <Space style={{ marginBottom: 24, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Title level={3} style={{ margin: 0 }}>Products</Title>
        <Select
          value={selectedCategory}
          onChange={setSelectedCategory}
          style={{ width: 200 }}
          options={[
            { value: 'all', label: 'All Categories' },
            ...categories.map(c => ({ value: c, label: c })),
          ]}
        />
      </Space>

      {loading ? (
        <div style={{ textAlign: 'center', padding: 80 }}>
          <Spin size="large" />
        </div>
      ) : products.length === 0 ? (
        <Empty description="No products found" />
      ) : (
        <Row gutter={[16, 16]}>
          {products.map(product => (
            <Col key={product.id} xs={24} sm={12} md={8} lg={6}>
              <ProductCard product={product} />
            </Col>
          ))}
        </Row>
      )}
    </div>
  )
}
