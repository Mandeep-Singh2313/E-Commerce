import { Pagination } from "@mui/material";
import { useNavigate, useSearchParams, useLocation } from "react-router-dom";

const Paginations = ({ numberOfPage, totalProducts }) => {
    const [searchParams] = useSearchParams();
    const location = useLocation();
    const pathname = location.pathname;
    const params = new URLSearchParams(searchParams);
    const navigate = useNavigate();
    const paramValue = searchParams.get("page") ? Number(searchParams.get("page")) : 1;

    const onChangeHandler = (event, value) => {
        params.set("page", value.toString());
        const url = `${pathname}?${params.toString()}`;
        console.log("🔥 Pagination - Navigating to:", url, { pathname, paramValue: value });
        navigate(url);
    };

    return (
        <Pagination
            count={numberOfPage}
            page={paramValue}
            defaultPage={1}
            siblingCount={0}
            boundaryCount={2}
            shape="rounded"
            onChange={onChangeHandler}
        />
    )
};
export default Paginations;