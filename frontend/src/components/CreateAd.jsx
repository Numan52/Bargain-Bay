import React, { useState } from 'react'
import { getJwt } from '../util.js/jwtUtils'
import { postAd } from "../api/adsApi"
import { useNavigate } from 'react-router-dom'
import Header from './Header'
import "../css/createAd.css"
import AdCategories from './AdCategories'
const apiUrl = import.meta.env.VITE_API_URL

const CreateAd = () => {
    const [errorMsg, setErrorMsg] = useState("")
    const [submitted, setSubmitted] = useState(false)
    const [selectedCategory, setSelectedCategory] = useState(null)
    console.log(selectedCategory)
    const navigate = useNavigate()

    const [images, setImages] = useState([])
    const [adData, setAdData] = useState({
        price: "",
        title: "",
        condition: "",
        description: "",
        categoryId: null
    })
    
    
    function handleFileChange(event) {
        console.log([...event.target.files])
        if (event.target.files) {
            setImages([...event.target.files])
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
        event.preventDefault()
        if (submitted) {
            return
        }

        if (selectedCategory === null) {
            setErrorMsg("Please select one category")
            window.scrollTo(0, 0)
            return
        }

        setSubmitted(true)

        setErrorMsg("")
        event.preventDefault();

        const form = new FormData() 
        
        const adDataJson = new Blob([JSON.stringify(adData)], { type: "application/json" });
        form.append("ad", adDataJson);

        for (let i = 0; i < images.length; i++) {
            form.append("images", images[i]);
        }

        try {
            await postAd(form)
            navigate("/success", {state: { title: adData.title}})
            
        } catch (error) {
            console.log("error while creating ad: ", error)
            setErrorMsg("An error ocurred. Please try again later")
        } finally {
            setSubmitted(false)
        }
    }

        
    return (
        <div className='create-ad__container'>
            <Header />
            <form onSubmit={submitAd} className='create-ad__form'>

                {errorMsg && 
                    <div className='error-message'>
                        {errorMsg}
                    </div>
                }
                <div className='file-container'>
                    <label className='create-ad__section-label' htmlFor="images">Pictures</label>
                    <input className='create-ad__input' type="file" id='images' name='images' onChange={handleFileChange} multiple/>
                </div>

                <div className='price-container'>
                    <label className='create-ad__section-label' htmlFor="price">Price</label> <br />
                    <div className='create-ad__price'>
                        <div>€</div>
                        <input
                            type='number'
                            className='create-ad__input'
                            inputMode='decimal' 
                            id='price'
                            name='price'
                            value={adData.price}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    
                </div>
                <div className='title-container'>
                    <label className='create-ad__section-label' htmlFor="title">Title</label> <br />
                    <input 
                        className='create-ad__input'
                        type="text" 
                        id='title'
                        name='title'
                        value={adData.title}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className='condition-container'>
                    <label className='create-ad__section-label' >Condition</label>
                    <div className='create-ad__conditions-container'>
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
                    
                
                </div>

                <div className='categories__container'>
                    <label className='create-ad__section-label' htmlFor="">Category</label>
                    <AdCategories 
                        selectedCategory={selectedCategory} 
                        onCategorySelected={(category) => {
                            setSelectedCategory(category)
                            setAdData((oldForm) => (
                                {
                                    ...oldForm,
                                    ['categoryId']: category?.id
                                }
                            ))
                        }} 
                        jumpToSearch={false}
                    />
                </div>

                <div className='description-container'>
                    <label className='create-ad__section-label' htmlFor="description">Description</label> <br />
                    <textarea 
                        className='create-ad__textarea'
                        name="description" 
                        id="description"
                        value={adData.description}
                        onChange={handleInputChange}
                        required
                    >

                    </textarea>
                </div>
                <button className='create-ad__submit-ad-btn'>Create Ad</button>

                
            </form>
            
        </div>
    )
}

export default CreateAd
