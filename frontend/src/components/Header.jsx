import { jwtDecode } from 'jwt-decode'
import React, { useContext, useEffect, useState } from 'react'
import { getDecodedJwt } from '../util.js/jwtUtils'
import "../css/header.css"
import { Link, useNavigate } from 'react-router-dom'
import { UserContext } from '../Context/UserContext'


const Header = () => {
    let userInfo = useContext(UserContext)
    const navigate = useNavigate()
    const [sidebarOpen, setSidebarOpen] = useState(false) 


    return (
        <>
            <header className='header-container'>
                <Link to={"/"}>
                    <h1>Bargain-Bay</h1>
                </Link>
                
                
                <div className='header-right-container'>
                    <Link to={"/create-ad"} className='header-create-ad-container'>
                        <img src="/plus.png" alt="" />
                        <span className='header-text'>Create Ad</span>
                    </Link>
                    
                    <Link to={"/chats"} className='header-chat-container'>
                        <img src="/chat.png" alt="" />
                        <span className='header-text'>Chats</span>
                    </Link>

                    
                        <div className='header-profile-container' onClick={() => setSidebarOpen(!sidebarOpen)}>
                            <img className='header-profile-pic' src="/user.png" alt="" />
                            <div className='header-username header-text'>
                                {userInfo.username}
                            </div>

                            {!userInfo.userId &&
                                <button className='login-button header-text' onClick={() => navigate("/login")}>
                                    <span >Login</span>
                                </button>
                            }

                        </div> 
                    

                    
                    {userInfo.userId &&
                        <button className='login-button header-text' onClick={() => {
                            localStorage.removeItem("token")
                            location.reload()
                        }}>
                            <span >Logout</span>
                        </button>                                                                                                                                                                                                                                                                                                                                                                                        
                    }

                    
                </div>
            </header>

             {/* Sidebar for Mobile */}
             <div className={`sidebar ${sidebarOpen ? "open" : ""}`}>
                <button className="close-btn" onClick={() => setSidebarOpen(!sidebarOpen)}>&times;</button>
                 {userInfo.userId && <p>Welcome, {userInfo.username}</p>}

                {!userInfo.userId &&
                        <button className='login-button ' onClick={() => navigate("/login")}>
                            <span>Login</span>
                        </button>
                    }

                {userInfo.userId &&
                    <button className='login-button' onClick={() => {
                        localStorage.removeItem("token")
                        location.reload()
                    }}>
                        <span >Logout</span>
                    </button>                                                                                                                                                                                                                                                                                                                                                                                        
                }
            </div>


        </>
        
    )
}

export default Header