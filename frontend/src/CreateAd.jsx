import React, { useState } from 'react'
import { getJwt } from './jwtUtils'
const apiUrl = import.meta.env.VITE_API_URL

const CreateAd = () => {
    const [errorMsg, setErrorMsg] = useState("")

    const [images, setImages] = useState([])
    const [adData, setAdData] = useState({
        price: "",
        title: "",
        condition: "",
        description: ""
    })
    
    
    function handleFileChange(event) {
        console.log([...event.target.files])
        if (event.target.files) {
            setImages([event.target.files])
        }
    }


    function handleInputChange(event) {
        const {name, value} = event.target
        console.log(event.target)
        setAdData((oldForm) => (
            {
                ...oldForm,
                [name]: value
            }
        ))
    }


    async function submitAd(event) {
        setErrorMsg("")
        event.preventDefault();

        const form = new FormData() 
        
        const adDataJson = new Blob([JSON.stringify(adData)], { type: "application/json" });
        form.append("ad", adDataJson);

        for (let i = 0; i < images.length; i++) {
            form.append("images", images[i]);
        }

        console.log(form)
        console.log(images)
        try {
            const response = await fetch(`${apiUrl}/ads`, {
              method: "POST",
              headers: { 
                "Authorization": `Bearer ${getJwt()}`
              },
              body: form
            })
          
            if(response.ok) {
                console.log("success")
            } else {
                const errorMessage = await response.json()
                setErrorMsg("An error occurred. Pls try again laer")
                console.log("response: ", errorMessage)
            }
        
          } catch (error) {
                console.log("error while creating ad: ", error)
                setErrorMsg("An error ocurred. Please try again later")
          }
    }

        
    return (
        <div>
            <form onSubmit={submitAd}>
                <div className='file-container'>
                    <label htmlFor="images"></label>
                    <input type="file" id='images' name='images' onChange={handleFileChange} multiple/>
                </div>

                <div className='price-container'>
                    <label htmlFor="price">Price</label> <br />
                    <div>€</div>
                    <input 
                        inputMode='decimal' 
                        id='price'
                        name='price'
                        value={adData.price}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className='title-container'>
                    <label htmlFor="title">Title</label> <br />
                    <input 
                        type="text" 
                        id='title'
                        name='title'
                        value={adData.title}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className='condition-container'>
                    <div>Condition</div>
                    {["NEW", "LIKE NEW", "USED", "BROKEN"].map((condition) => (
                        <div key={condition}>
                            <input 
                                type="radio"
                                name='condition'
                                id={condition.toLowerCase()}
                                value={condition}
                                checked={condition === adData.condition}
                                onChange={handleInputChange}
                            />
                            <label htmlFor={condition.toLocaleLowerCase()}>{condition}</label> <br />
                        </div>
                    ))}
                
                </div>

                <div className='description-container'>
                    <label htmlFor="description">Description</label> <br />
                    <textarea 
                        name="description" 
                        id="description"
                        value={adData.description}
                        onChange={handleInputChange}
                        required
                    >

                    </textarea>
                </div>
                <button className='submit-ad-btn'>Create Ad</button>

                {errorMsg && 
                    <div>
                        {errorMsg}
                    </div>
                }
            </form>
            
        </div>
    )
}

export default CreateAd
