import { useState } from "react";
import axios from "axios";
import OutlinedInput from '@mui/material/OutlinedInput';
import { Button, Divider, ListItemIcon, ListItemText } from "@mui/material";
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import { IoLocationSharp } from "react-icons/io5";
import './Distance_Page.css'; // Import file CSS
import { getDistance } from 'geolib'; // Import geolib for distance calculation

const NONIMATIM_BASE_URL = "https://nominatim.openstreetmap.org/search?";

export default function SearchBox(props) {
    const { selectPosition, setSelectPosition } = props; // eslint-disable-line
    const [searchText, setSearchText] = useState("");
    const [listPlace, setListPlace] = useState([]);
    const [distance, setDistance] = useState(null); // To store calculated distance
    const [isListVisible, setIsListVisible] = useState(true); // Trạng thái hiển thị danh sách

    // TP.HCM, Việt Nam
    const position = [10.7763897, 106.7011391];

    // Hàm xử lý tìm kiếm
    const handleSearch = () => {
        const params = {
            q: searchText,
            format: 'json',
            addressdetails: 1,
            polygon_geojson: 0,
        };

        const queryString = new URLSearchParams(params).toString();
        const requestOption = {
            method: "GET",
            redirect: "follow"
        };

        fetch(`${NONIMATIM_BASE_URL}${queryString}`, requestOption)
            .then((response) => response.text())
            .then((result) => {
                const parsedResult = JSON.parse(result);
                setListPlace(parsedResult);  // Cập nhật danh sách địa điểm tìm thấy
                setIsListVisible(true); // Hiển thị lại danh sách sau khi tìm kiếm
            })
            .catch((err) => {
                console.error("Error: ", err);
            });
    };

    // Hàm tính khoảng cách
    const calculateDistance = (lat1, lon1, lat2, lon2) => {
        const distanceInMeters = getDistance(
            { latitude: lat1, longitude: lon1 },
            { latitude: lat2, longitude: lon2 }
        );
        return distanceInMeters;
    };

    // Hàm xử lý khi chọn vị trí
    const handleSelectPosition = (item) => {
        setSelectPosition(item);

        // Lấy tọa độ của địa điểm được chọn
        const selectedLat = parseFloat(item.lat);
        const selectedLon = parseFloat(item.lon);

        // Tính khoảng cách từ vị trí cố định (position) đến địa điểm đã chọn
        const calculatedDistance = calculateDistance(position[0], position[1], selectedLat, selectedLon);
        setDistance(calculatedDistance); // Cập nhật khoảng cách

        // Ẩn danh sách sau khi chọn địa chỉ
        setIsListVisible(false);
    };

    // Hàm xử lý thanh toán (dummy function)
    const handlePayment = () => {
        const paymentData = {
            distance: distance, // Khoảng cách
            source: "TP.HCM",  // Nguồn cố định
            destination: selectPosition?.display_name, // Đích đến
        };

        axios.post("http://localhost:8080/api/payment", paymentData, {
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                const data = response.data;
                if (data.success) {
                    alert("Thanh toán thành công!");
                } else {
                    alert("Thanh toán thất bại!");
                }
            })
            .catch((error) => {
                console.error("Error during payment:", error);
                alert("Có lỗi xảy ra trong quá trình thanh toán.");
            });
    };

    return (
        <>
            <div className="search-box-container">
                <div className="search-box-input-container">
                    <div style={{ flex: 1 }}>
                        <OutlinedInput
                            className="search-box-input"
                            style={{ width: '100%' }}
                            placeholder="Nhập địa điểm cần tìm..."
                            value={searchText}
                            onChange={(event) => {
                                setSearchText(event.target.value);
                            }}
                        />
                    </div>
                    <div className="search-box-button-container">
                        <Button variant="contained" color="primary" onClick={handleSearch}>
                            Tìm kiếm
                        </Button>
                    </div>
                </div>

                {isListVisible && listPlace.length > 0 && (
                    <List component="nav" aria-label="main mailbox folders" className="search-box-list">
                        {
                            listPlace.map((item) => {
                                return (
                                    <div key={item?.osm_id}>
                                        <ListItem
                                            button
                                            onClick={() => handleSelectPosition(item)}>
                                            <ListItemIcon>
                                                <IoLocationSharp size={30} color="Blue" />
                                            </ListItemIcon>
                                            <ListItemText primary={item?.display_name} />
                                        </ListItem>
                                        <Divider />
                                    </div>
                                );
                            })
                        }
                    </List>
                )}

                {distance !== null && (
                    <div className="distance-result">
                        <h3>Khoảng cách từ địa điểm bạn nhập đến Trung tâm TP.HCM: {distance} mét</h3>
                        <Button
                            variant="contained"
                            color="secondary"
                            onClick={handlePayment}>
                            Thanh toán
                        </Button>
                    </div>
                )}
            </div>
        </>
    );
}
