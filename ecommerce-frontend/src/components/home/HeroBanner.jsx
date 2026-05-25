import {Swiper,SwiperSlide} from "swiper/react"

import {Navigation, EffectFade, Autoplay, Pagination} from 'swiper/modules';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/scrollbar';
import 'swiper/css/effect-fade';
import 'swiper/css/autoplay';
import 'swiper/css';
import { Link } from "react-router-dom";
import { fetchProducts } from "../../store/actions";
import { getImageUrl } from "../../utils/imageUrl";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

const gradients=[
    "linear-gradient(135deg, #b8956a 0%, #d4af37 100%)",
    "linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%)",
    "linear-gradient(135deg, #a0826d 0%, #d4a574 100%)",
    "linear-gradient(135deg, #36454f 0%, #5a5a5a 100%)"
];

const HeroBanner = () =>{
    const dispatch = useDispatch();
    const { products } = useSelector((state) => state.products);

    useEffect(() => {
        if (!products || products.length === 0) {
            dispatch(fetchProducts());
        }
    }, [dispatch, products]);

    // Filter products with id 1, 2, 3, 4
    const bannerProducts = products?.filter(product => 
        [1, 2, 3, 4].includes(product.productId)
    )?.slice(0, 4) || [];

    return(
        <div className="py-2 rounded-mid">
            <Swiper grabCursor={true} autoplay={{
                delay:4000, disableOnInteraction:false
            }}
            navigation
            modules={[Pagination, EffectFade, Navigation, Autoplay]}
            pagination={{ clickable:true }}
            scrollbar={{ draggable: true }}
            slidesPerView={1}>
                {bannerProducts.map((item,i)=>(
                    <SwiperSlide key={item.productId}>
                        <div className="carousel-item rounded-md sm:h-[500px] h-96" style={{background: gradients[i]}}>
                        <div className="flex items-center justify-center">
                            <div className="hidden lg:flex justify-center w-1/2 p-8">
                            <div className="text-center">
                                <h3 className="text-3xl text-white font-bold">
                                    {item.productName}
                                </h3>
                                <h1 className="text-5xl text-white font-bold mt-2">
                                    Premium Product
                                </h1>
                                <p className="text-white font-bold mt-4">
                                    {item.description?.substring(0, 60)}
                                </p>
                                <Link
                                    className="mt-6 inline-block bg-black text-white py-2 px-4 rounded hover:bg-gray-800"
                                    to="/products">
                                Shop
                                </Link>
                            </div>
                        </div>
                        <div className="w-full flex justify-center lg:w-1/2 p-4">
                            <img src={getImageUrl(item?.image)} className="w-[400px] h-[450px] object-cover"></img>
                        </div>
                        </div>
                        </div>
                    </SwiperSlide>
                ))}
            </Swiper>
        </div>
    )
}
export default HeroBanner;