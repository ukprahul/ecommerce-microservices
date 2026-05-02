import { Layout, Menu, Button, Space, Tag } from 'antd'
import { ShoppingOutlined, AppstoreOutlined, UserOutlined } from '@ant-design/icons'
import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { logout as apiLogout } from '../api/userApi'

const { Header } = Layout

export default function Navbar() {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, token, logout, isAdmin } = useAuth()

  const handleLogout = async () => {
    try {
      if (token && user?.id) {
        await apiLogout(token, user.id)
      }
    } catch {
      // proceed with client-side logout even if server call fails
    }
    logout()
    navigate('/login')
  }

  const menuItems = [
    { key: '/', label: 'Catalog', icon: <ShoppingOutlined /> },
    ...(isAdmin ? [{ key: '/admin', label: 'Admin', icon: <AppstoreOutlined /> }] : []),
  ]

  return (
    <Header style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 24px' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 32 }}>
        <span style={{ color: '#fff', fontWeight: 700, fontSize: 18, cursor: 'pointer' }}
              onClick={() => navigate('/')}>
          ShopZone
        </span>
        <Menu
          theme="dark"
          mode="horizontal"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
          style={{ minWidth: 200, background: 'transparent' }}
        />
      </div>
      <Space>
        {user ? (
          <>
            <UserOutlined style={{ color: '#fff' }} />
            <span style={{ color: '#fff' }}>{user.email}</span>
            {isAdmin && <Tag color="gold">ADMIN</Tag>}
            <Button type="default" size="small" onClick={handleLogout}>Logout</Button>
          </>
        ) : (
          <>
            <Button type="default" size="small" onClick={() => navigate('/login')}>Login</Button>
            <Button type="primary" size="small" onClick={() => navigate('/signup')}>Sign Up</Button>
          </>
        )}
      </Space>
    </Header>
  )
}
