package com.project.ecommerce.service;

import com.project.ecommerce.entity.Cart;
import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.exception.APIException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.payload.CartDTO;
import com.project.ecommerce.payload.CartItemDTO;
import com.project.ecommerce.payload.ProductDTO;
import com.project.ecommerce.repo.CartItemRepository;
import com.project.ecommerce.repo.CartRepository;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
//import org.h2.engine.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, int quantity) {
        Cart cart=createCart();
        //Retrieve product details
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product",
                        "productId", productId));
        //perform validations
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId
                (cart.getCartId(), productId);
        if(cartItem!=null){
            throw  new APIException("Product "+product.getProductName()+
                    " already exists in cart.");
        }

        if(product.getQuantity()==0){
            throw  new APIException("Product "+product.getProductName()+
                    " is not available.");
        }
        if(product.getQuantity()<quantity){
            throw  new APIException("Please make an order of the "
                    +product.getProductName()+
                    " less than or equal to "+product.getQuantity()+".");
        }
        //create new cart item
        CartItem newCartItem=new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity()); //-quantity
        cart.setTotalPrice(cart.getTotalPrice()+(quantity*product.getSpecialPrice()));
        Cart savedCart=cartRepository.save(cart);
        CartDTO cartDTO= modelMapper.map(savedCart, CartDTO.class);
        List<CartItem> cartItems=cart.getCartItems();
        Stream<ProductDTO> productDTOStream=cartItems.stream().map(item->{
            ProductDTO map=modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    private Cart createCart(){
        //Find existing cart or create one if not exists
        Cart userCart=cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null){
            return userCart;
        }
        Cart cart=new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new APIException("No cart exists.");
        }
        List<CartDTO> cartDTOs=carts.stream().map(cart->{
            CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products=cart.getCartItems().stream().map(cartItem->{
                ProductDTO productDTO=modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                productDTO.setQuantity(cartItem.getQuantity());
                return productDTO;
        }).toList();
            cartDTO.setProducts(products);
            return cartDTO;
        }).toList();
     return cartDTOs;
}

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart=cartRepository.findCartByEmailAndCartId(emailId,cartId);
        if(cart==null){
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }
        CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products=cart.getCartItems().stream().
                map(p->modelMapper.map(p.getProduct(),
                        ProductDTO.class)).toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId=authUtil.loggedInEmail();
        Cart userCart=cartRepository.findCartByEmail(emailId);
        Long cartId=userCart.getCartId();
        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart","cartId",cartId));
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product",
                        "productId", productId));
        if(product.getQuantity()==0){
            throw  new APIException("Product "+product.getProductName()+
                    " is not available.");
        }
        if(product.getQuantity()<quantity){
            throw  new APIException("Please make an order of the "
                    +product.getProductName()+
                    " less than or equal to "+product.getQuantity()+".");
        }
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId
                (cartId, productId);
        if(cartItem==null){
            throw new APIException("Product "+product.getProductName()+
                    " does not exist in cart.");
        }
        int newQuantity=cartItem.getQuantity()+quantity;
        if(newQuantity<0){
            throw new APIException("Product "+product.getProductName()+
                    " quantity in cart cannot be negative.");
        }
        if(newQuantity==0){
            deleteProductFromCart(cartId, productId);
        }
        else{
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice()+(quantity*cartItem.getProductPrice()));
            cartRepository.save(cart);
        }
        CartItem updatedItem=cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity()==0){
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }
        CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems=cart.getCartItems();
        Stream<ProductDTO> productStream=cartItems.stream().map(item->{
            ProductDTO prd=modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart","cartId",cartId));
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId
                (cartId, productId);
        if(cartItem==null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice()-
                (cartItem.getQuantity()*cartItem.getProductPrice()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
        return "Product "+cartItem.getProduct().getProductName()+" removed from cart.";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart","cartId",cartId));
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product",
                        "productId", productId));
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId
                (cartId, productId);
        if(cartItem==null) {
            throw new APIException("Product "+product.getProductName()+
                    " does not exist in cart.");
        }
        double cartPrice=cart.getTotalPrice()-
                (cartItem.getQuantity()*cartItem.getProductPrice());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice+(
                    cartItem.getQuantity()*cartItem.getProductPrice()
                ));
        cartItem=cartItemRepository.save(cartItem);

    }

    @Transactional
    @Override
    public String createOrUpdateCartWithItems(List<CartItemDTO> cartItems) {
        // Get user's email
        String emailId = authUtil.loggedInEmail();

        // Check if an existing cart is available or create a new one
        Cart existingCart = cartRepository.findCartByEmail(emailId);
        if (existingCart == null) {
            existingCart = new Cart();
            existingCart.setTotalPrice(0.00);
            existingCart.setUser(authUtil.loggedInUser());
            existingCart = cartRepository.save(existingCart);
        } else {
            // Clear all current items in the existing cart
            cartItemRepository.deleteAllByCartId(existingCart.getCartId());
        }

        double totalPrice = 0.00;

        // Process each item in the request to add to the cart
        for (CartItemDTO cartItemDTO : cartItems) {
            Long productId = cartItemDTO.getProductId();
            Integer quantity = cartItemDTO.getQuantity();

            // Find the product by ID
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

            // Directly update product stock and total price
            // product.setQuantity(product.getQuantity() - quantity);
            totalPrice += product.getSpecialPrice() * quantity;

            // Create and save cart item
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(existingCart);
            cartItem.setQuantity(quantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cartItemRepository.save(cartItem);
        }

        // Update the cart's total price and save
        existingCart.setTotalPrice(totalPrice);
        cartRepository.save(existingCart);
        return "Cart created/updated with the new items successfully";
    }


}
