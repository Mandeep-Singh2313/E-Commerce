import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { getOrdersForDashboard } from "../store/actions";

const useOrderFilter = () => {
    console.log("=== useOrderFilter Hook Called ===");
    
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    const { user } = useSelector((state) => state.auth);
    const isAdmin = user && user?.roles?.includes("ROLE_ADMIN");
    
    console.log("=== useOrderFilter Hook Rendered ===");
    console.log("User:", user);
    console.log("isAdmin:", isAdmin);

    useEffect(() => {
        console.log("=== useOrderFilter Effect Triggered ===");
        console.log("searchParams:", searchParams);
        
        const params = new URLSearchParams();

        const currentPage = searchParams.get("page")
            ? Number(searchParams.get("page"))
            : 1;

        params.set("pageNumber", currentPage - 1);

        const queryString = params.toString();
        console.log("QUERY STRING", queryString);
        console.log("About to dispatch getOrdersForDashboard with isAdmin:", isAdmin);
        
        dispatch(getOrdersForDashboard(queryString, isAdmin));

    }, [dispatch, searchParams, isAdmin]);
};

export default useOrderFilter;