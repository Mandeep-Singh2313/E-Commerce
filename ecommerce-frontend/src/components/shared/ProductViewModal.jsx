import { Button, Dialog, DialogBackdrop, DialogPanel, DialogTitle } from '@headlessui/react'
import { Divider } from '@mui/material';
import { useState } from 'react'
import { MdClose, MdDone } from 'react-icons/md';
import Status from './Status';
import { getImageUrl } from '../../utils/imageUrl';

function ProductViewModal({open,setOpen,product,isAvailable}) {
  const {id, productName,image,description,quantity,price,
        discount,specialPrice}=product;
  
  // Determine if product is available based on isAvailable prop or quantity
  const inStock = isAvailable !== undefined ? isAvailable : (quantity > 0);
  
  const handleClickOpen=()=>{
        setOpen(true)
  }

  return (
    <>
      <Dialog open={open} as="div" className="relative z-10" onClose={() => setOpen(false)}>
        <DialogBackdrop className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
        <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4">
            <DialogPanel
              transition
              className="relative transform overflow-hidden rounded-lg bg-white shadow-xl transition-all w-full max-w-md sm:max-w-lg"
            >
                {image && (
                    <div className='flex justify-center aspect-[3/2]'>
                      <img
                        src={getImageUrl(image)}
                        alt={productName}
                      />
                    </div>
                )}

              <div className='px-6 pt-10 pb-2'>
                <DialogTitle as="h3" className="lg:text-3xl sm:text-2xl text-xl 
                font-semibold leading-6 text-gray-800 mb-4">
                {productName}
              </DialogTitle></div>
                
                <div className="space-y-4 text-gray-700 pb-4">
                  <div className="flex items-center justify-between gap-2">
                    {specialPrice ? (
                    <div className="flex items-center gap-2">
                      <span className="text-gray-400 line-through">
                        Rs.{Number(price).toFixed(2)}
                      </span>
                      <span className="sm:text-xl font-semibold text-slate-700">
                        Rs.{Number(specialPrice).toFixed(2)}
                      </span>
                    </div>) : (
                      <span className='text-xl font-bold'>
                        Rs.{Number(price).toFixed(2)}
                      </span>
                    )}
                    {inStock ? (
                      <Status
                        text="In Stock"
                        icon={MdDone}
                        bg="bg-teal-200"
                        color="text-teal-900"
                      />
                    ) : (
                      <Status
                        text="Out-Of-Stock"
                        icon={MdClose}
                        bg="bg-rose-200"
                        color="text-rose-700"
                      />
                    )}
                  </div>
                  <Divider />
                  <p>{description}</p>
                </div>
                <div className="px-6 py-4 flex justify-end gap-4">
                    <button 
                    onClick={()=>setOpen(false)}
                    type="button"
                    className="px-4 py-2 text-sm font-semibold text-slate-700">
                      Close
                    </button>
                </div>
            </DialogPanel>
          </div>
        </div>
      </Dialog>
    </>
  )
}

export default  ProductViewModal;