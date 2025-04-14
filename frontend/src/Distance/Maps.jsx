import { MapContainer, Marker, Popup, TileLayer, useMap, Polyline } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { IoLocationSharp } from "react-icons/io5";
import L from "leaflet";
import { renderToStaticMarkup } from "react-dom/server";
import { useEffect } from "react";
import "./Distance_Page.css";
import {MAPTILER_KEY} from "../config/MapConfig.js";


const apiKey = MAPTILER_KEY;
const position = [10.7763897, 106.7011391];// Tọa độ mặc định của thành phố hồ chí minh nha ní

// Cái này dùng để biến các icon trong thư viện react thành các icon trong leaflet cho phần giao diện địa chỉ trên map
export function CustomIcon() {
    const svg = renderToStaticMarkup(<IoLocationSharp size={30} color="Blue"/>);
    return L.divIcon({
        className: 'custom-icon',
        html: svg,
        iconSize: [30, 30], // Kích thước icon
        iconAnchor: [15, 30], // Mũi tên chỉ xuống dưới
    });
}


function ResetCenterView(props){
    const { selectPosition} = props;// eslint-disable-line
    const map = useMap();

    useEffect(() => {
        if(selectPosition){
            map.setView(
                L.latLng(selectPosition?.lat, selectPosition?.lon),// eslint-disable-line
                map.getZoom(),
                {
                    animate: true,
                }
            )
        }   
    }, [selectPosition]);// eslint-disable-line

    return null;
    
}

export default function Maps(props) {
    const { selectPosition } = props; // eslint-disable-line
    const locationSelection = selectPosition ? [selectPosition.lat, selectPosition.lon] : null; // eslint-disable-line
    
    return (
        <>
            <MapContainer center={position} zoom={13} scrollWheelZoom={false} style={{ width: "100%", height: "100%" }}>
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    url={`https://api.maptiler.com/maps/basic-v2/256/{z}/{x}/{y}.png?key=${apiKey}`}
                />
                <Marker position={position} icon={CustomIcon()}>
                    <Popup>
                        Default Position
                    </Popup>
                </Marker>
                
                {selectPosition && (
                    <Marker position={locationSelection} icon={CustomIcon()}>
                        <Popup>
                            Selected Location
                        </Popup>
                    </Marker>
                )}

                {/* Only draw the polyline if both markers are available */}
                {selectPosition && locationSelection && (
                    <Polyline positions={[position, locationSelection]} color="blue" />
                )}

                <ResetCenterView selectPosition={selectPosition} />
            </MapContainer>
        </>
    );
}


