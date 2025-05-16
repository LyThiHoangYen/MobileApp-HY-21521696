import React, { useEffect, useState } from "react";
import {
  View,
  Dimensions,
  StyleSheet,
  Text,
  Image,
  TouchableOpacity,
  FlatList,
  Alert,
} from "react-native";
import { useIsFocused } from '@react-navigation/native';

import { auth, db, doc, getDoc, updateDoc } from '../../../firebase';
import CheckOut from "../../component/CartDetail/CheckOut";

const Cart = () => {
  const isFocused = useIsFocused();
  const [cartList, setCartList] = useState([]);

  useEffect(() => {
    if (isFocused) {
      fetchCartItems();
    }
  }, [isFocused]);

  const fetchCartItems = async () => {
    try {
      const userId = auth.currentUser?.uid;
      if (!userId) return;
      const docSnap = await getDoc(doc(db, 'users', userId));
      if (docSnap.exists()) {
        setCartList(docSnap.data().cart || []);
      }
    } catch (err) {
      console.error('Error fetching cart items:', err);
    }
  };

  const updateCart = async (newCart) => {
    const userId = auth.currentUser?.uid;
    if (!userId) return;
    await updateDoc(doc(db, 'users', userId), { cart: newCart });
    setCartList(newCart);
  };

  const incrementQty = (item) => {
    const updatedCart = cartList.map(prod =>
      prod.id === item.id ? { ...prod, qty: prod.qty + 1 } : prod
    );
    updateCart(updatedCart);
  };

  const decrementQty = (item, index) => {
    if (item.qty > 1) {
      const updatedCart = cartList.map(prod =>
        prod.id === item.id ? { ...prod, qty: prod.qty - 1 } : prod
      );
      updateCart(updatedCart);
    } else {
      removeItem(index);
    }
  };

  const removeItem = (index) => {
    Alert.alert(
      'Xác nhận',
      'Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng không?',
      [
        { text: 'Hủy', style: 'cancel' },
        {
          text: 'Xóa',
          style: 'destructive',
          onPress: async () => {
            const newCart = [...cartList];
            newCart.splice(index, 1);
            updateCart(newCart);
          }
        }
      ]
    );
  };

  const renderItem = ({ item, index }) => (
    <View style={styles.itemWrapper}>
      <View style={styles.foodcard}>
        <Image source={{ uri: item.image }} style={styles.foodimage} />
        <View style={styles.foodinfo}>
          <Text style={styles.foodName}>{item.name}</Text>
          <Text style={styles.foodPrice}>{item.price.toLocaleString()} ₫</Text>
        </View>
        <View style={styles.qtyContainer}>
          <View style={styles.qtyControls}>
            <TouchableOpacity onPress={() => decrementQty(item, index)}>
              <Image source={require('../../assets/VectorMinus.png')} />
            </TouchableOpacity>
            <Text style={styles.qtyText}>{item.qty}</Text>
            <TouchableOpacity onPress={() => incrementQty(item)}>
              <Image source={require('../../assets/VectorPlusMini.png')} />
            </TouchableOpacity>
          </View>
          <TouchableOpacity onPress={() => removeItem(index)}>
            <Text style={styles.deleteText}>Xóa</Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );

  return (
    <View style={{ flex: 1 }}>
      <FlatList
        data={cartList}
        renderItem={renderItem}
        keyExtractor={(item, index) => `${item.id}_${index}`}
        contentContainerStyle={{ paddingVertical: 10 }}
        showsVerticalScrollIndicator={false}
      />
      <CheckOut />
    </View>
  );
};

const deviceWidth = Math.round(Dimensions.get("window").width);

const styles = StyleSheet.create({
  itemWrapper: {
    paddingBottom: 15,
  },
  foodcard: {
    flexDirection: "row",
    width: deviceWidth - 40,
    height: 90,
    backgroundColor: "#fff",
    alignSelf: 'center',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 12,
    shadowColor: "#000",
    shadowOffset: { width: 5, height: 5 },
    shadowOpacity: 0.75,
    shadowRadius: 5,
    elevation: 5,
    paddingHorizontal: 10,
  },
  foodinfo: {
    justifyContent: 'center',
    width: '50%',
    marginLeft: 15,
  },
  foodimage: {
    width: 80,
    height: 80,
    borderRadius: 12,
  },
  foodName: {
    fontSize: 15,
  },
  foodPrice: {
    fontSize: 15,
    fontWeight: '600',
  },
  qtyContainer: {
    alignItems: 'center',
    justifyContent: 'space-between',
    height: '100%',
    paddingVertical: 10,
  },
  qtyControls: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  qtyText: {
    fontWeight: '600',
    fontSize: 17,
    marginHorizontal: 8,
  },
  deleteText: {
    color: 'red',
    marginTop: 4,
  }
});

export default Cart;
