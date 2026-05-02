import { Card, Tag, Typography } from 'antd'
import { useNavigate } from 'react-router-dom'

const { Text } = Typography

export default function ProductCard({ product }) {
  const navigate = useNavigate()

  return (
    <Card
      hoverable
      style={{ width: '100%' }}
      cover={
        <img
          alt={product.title}
          src={product.imageUrl}
          style={{ height: 200, objectFit: 'contain', padding: 16, background: '#fafafa' }}
        />
      }
      onClick={() => navigate(`/products/${product.id}`)}
    >
      <Card.Meta
        title={<Text ellipsis style={{ maxWidth: '100%' }}>{product.title}</Text>}
        description={
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 8 }}>
            <Text strong style={{ fontSize: 16 }}>${product.price?.toFixed(2)}</Text>
            {product.category?.title && (
              <Tag color="blue">{product.category.title}</Tag>
            )}
          </div>
        }
      />
    </Card>
  )
}
