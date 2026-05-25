
const btnStyles = "border-[1.2px] border-slate-800 px-3 py-1 rounded-sm cursor-pointer";
const SetQuantity = ({
    quantity,
    cardCounter,
    handleQtyIncrease,
    handleQtyDecrease,
}) => {
   return (
   <div className="flex gap-8 items-center">
        {cardCounter ? null : <div className="font-semibold">QUANTITY</div>}
        <div className="flex md:flex-row flex-col gap-4 items-center lg:text-[22px] text-sm">
            <button
                disabled={quantity<=1}
                className={btnStyles}
                onClick={() => {
                    console.log("🔥 Minus button clicked - quantity:", quantity);
                    handleQtyDecrease();
                }}>
                -
            </button>
                <div className="text-red-500">{quantity}</div>
            <button
                className={btnStyles}
                onClick={() => {
                    console.log("🔥 Plus button clicked - quantity:", quantity);
                    handleQtyIncrease();
                }}>
                +
            </button>
        </div>
    </div>
   );
};

export default SetQuantity;