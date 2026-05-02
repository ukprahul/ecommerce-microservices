import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { Layout } from 'antd'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'
import Catalog from './pages/Catalog'
import ProductDetail from './pages/ProductDetail'
import Admin from './pages/Admin'
import Login from './pages/Login'
import Signup from './pages/Signup'

const { Content } = Layout

export default function App() {
  return (
    <BrowserRouter>
      <Layout style={{ minHeight: '100vh' }}>
        <Navbar />
        <Content style={{ background: '#fff' }}>
          <Routes>
            <Route path="/" element={<Catalog />} />
            <Route path="/products/:id" element={<ProductDetail />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route
              path="/admin"
              element={
                <ProtectedRoute adminOnly>
                  <Admin />
                </ProtectedRoute>
              }
            />
          </Routes>
        </Content>
      </Layout>
    </BrowserRouter>
  )
}
