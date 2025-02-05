import { jwtDecode } from 'jwt-decode'
import React, { useEffect, useState } from 'react'
import { getDecodedJwt } from '../jwtUtils'
import "../css/header.css"
import { Link } from 'react-router-dom'

const Header = () => {
    const [username, setUsername] = useState("")

    useEffect(() => {
        let token = localStorage.getItem("token")
        if (token != null) {
            setUsername(getDecodedJwt(token).sub)
        }
    }, []) 
    

    return (
        <header className='header-container'>
            <h1>Unique Name</h1>
            
            <div className='header-right-container'>
                <Link to={"/create-ad"}>Create an Ad</Link>
                
                <div className='header-profile-container'>
                    <img className='header-profile-pic' src="/user.png" alt="" />
                    <div className='header-username'>
                        {username}
                    </div>
                </div> 
            </div>
        </header>
    )
}

export default Header