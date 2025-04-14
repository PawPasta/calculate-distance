import {useState} from "react";

import Maps from "./Maps.jsx";
import SearchBox from "./SearchBox.jsx";
import "./Distance_Page.css"

function Distance_Page() {
    const [selectPosition, setSelectPosition] = useState(null);
    return (
        <div className="distance-container">
            <div className="map-section">
                <Maps selectPosition={selectPosition} />
            </div>
            <div className="search-section">
                <SearchBox selectPosition={selectPosition} setSelectPosition={setSelectPosition} />
            </div>
        </div>
    );
}

export default Distance_Page;
