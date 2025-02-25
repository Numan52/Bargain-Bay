import React from 'react'

const NotFound = () => {
  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>404</h1>
      <p>Requested resource not found.</p>
      <a style={{ textDecoration: "underline"}} href="/">Back to the homepage</a>
    </div>
  )
}

export default NotFound