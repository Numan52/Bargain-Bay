import React, { createContext, useContext, useEffect, useRef, useState } from 'react'
import { Client } from '@stomp/stompjs';
import { UserContext } from './UserContext';

const SOCKET_URL = import.meta.env.VITE_WEB_SOCKET_URL

const WebSocketContext = createContext(null)

// TODO: disconnect on logout
const WebSocketProvider = ({children}) => {
    const userInfo = useContext(UserContext)

    const clientRef = useRef(null)
    const [messages, setMessages] = useState([])

    useEffect(() => {
        const token = localStorage.getItem("token")
        if(!token) {
            return
        }
        const client = new Client({
            brokerURL: SOCKET_URL,
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            onConnect: () => {
                console.log("connected via websocket")
                const subscription = client.subscribe(`/user/${userInfo.userId}/queue/messages`, (message) => {
                    console.log("message received: ", JSON.parse(message.body))
                    setMessages((prev) => [...prev, JSON.parse(message.body)])
                })
    
            },
            onWebSocketError: (frame) => {
                console.log("web socket error: ", frame)
            },
            onStompError: (frame) => {
                console.log('Broker reported error: ' + frame.headers['message']);
                console.log('Additional details: ' + frame.body);
            }
        })
        client.forceDisconnect
        clientRef.current = client
        client.activate()
    }, [userInfo])
    
    
    function disconnectWebSocket() {
        if (clientRef.current) {
            clientRef.current.deactivate();
            console.log("WebSocket disconnected");
        }
    }
    
    
    function publishMessage(receiverId, message) {
        console.log("message: ", message)
        clientRef.current.publish({
            destination: "/app/chat",
            body: JSON.stringify(message)
        })
    }

  return (
    <WebSocketContext.Provider value={{clientRef, messages, publishMessage, disconnectWebSocket }}>
        {children}
    </WebSocketContext.Provider>
        
    
  )
}

export {WebSocketProvider, WebSocketContext}
