import axios from "axios";


export const postDistance = async (lat, lon) =>{
    try {
        const respones  = await axios.post("",{
            lat,
            lon
        });
        return respones.data;
    } catch (error){
        console.error("Error Posting Data", error);
    }
}