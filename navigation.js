import React from 'react'
import { createStackNavigator } from '@react-navigation/stack';
//import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { NavigationContainer } from '@react-navigation/native';
//import { TouchableOpacity, View } from 'react-native';
//import { useNavigation } from '@react-navigation/native';

import Home from './src/screen/Home';
import Menu from './src/screen/Menu';
import FoodDetail from './src/screen/FoodDetail';
import ChooseSuccessfully from "./src/screen/VoucherScreen/ChooseSuccessfully";
import VoucherDescription from "./src/screen/VoucherScreen/VoucherDescription";
import Sort from "./src/screen/Sort";
import Account from './src/screen/Account';

import ReviewDescription from './src/screen/Rating/ReviewDescription';
import ThongTin from './src/screen/ThongTin';
import DoiMatKhau from './src/screen/DoiMatKhau';
import XacThuc from './src/screen/XacThuc'
import Voucher from "./src/screen/VoucherScreen/Voucher";

import SortReview from "./src/component/Sort/SortReview";
import SortMoney from "./src/component/Sort/SortMoney";
import DetailReview from "./src/component/FoodDetail/DetailReview";
import OrderTracking from './src/screen/OrderTracking';

import Cart from './src/screen/CartScreen/Cart';
import OrderConfirm from './src/screen/Checkout/OrderConfirm';
import AddAddress from './src/screen/Checkout/AddAddress';
import Address from './src/screen/Checkout/Address';
import OrderStatus from './src/screen/Checkout/OrderStatus';

//import { FontAwesome5 } from '@expo/vector-icons';


const Stack = createStackNavigator();

const HomeStack = () => {
    console.log(Stack);
    return (
        <NavigationContainer independent={true}>
            <Stack.Navigator initialRouteName='Trang chủ'>
                <Stack.Screen name="Trang chủ" component={Home} options={{ headerShown: false, }} />
                <Stack.Screen name="Sort" component={Sort} />
                <Stack.Screen name="SortReview" component={SortReview} />
                <Stack.Screen name="SortMoney" component={SortMoney} />
                <Stack.Screen name="FoodDetail" component={FoodDetail}
                    options={{
                        title: 'Chi tiết sản phẩm',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, paddingBottom: 10, },
                        headerLeftContainerStyle: { paddingBottom: 10, },
                    }}
                />
                <Stack.Screen name="ReviewStack" component={ReviewStack}
                    options={{
                        title: 'Đánh giá',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'left',
                        headerTitleStyle: { fontSize: 18, paddingBottom: 10, },
                        headerLeftContainerStyle: { paddingBottom: 10, },
                    }}
                />
                <Stack.Screen name="DetailReview" component={DetailReview} />
                <Stack.Screen name="Cart" component={Cart}
                    options={{
                        title: 'Giỏ hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, paddingBottom: 10, },
                        headerLeftContainerStyle: { paddingBottom: 10, },
                    }}
                />
                <Stack.Screen name="OrderConfirm" component={OrderConfirm}
                    options={{
                        title: 'Xác nhận đơn hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, paddingBottom: 10, },
                        headerLeftContainerStyle: { paddingBottom: 10, },
                    }}
                />
                <Stack.Screen name="OrderTracking" component={OrderTracking}
                    options={{
                        title: 'Theo dõi đơn hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, paddingBottom: 10, },
                        headerLeftContainerStyle: { paddingBottom: 10, },
                    }}
                />
            </Stack.Navigator>
        </NavigationContainer>
    )
}

const MenuStack = () => {
    console.log(Stack);
    return (
        <NavigationContainer independent={true}>
            <Stack.Navigator initialRouteName='Menu'>
                <Stack.Screen name="Menu" component={Menu}
                    options={{
                        headerShown: false,
                        title: 'Thực đơn',
                    }}

                />
                <Stack.Screen name="FoodDetail" component={FoodDetail}
                    options={{
                        title: 'Chi tiết sản phẩm',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, },
                    }}

                />
                <Stack.Screen name="Lọc" component={Sort} />
                <Stack.Screen name="Giá tiền" component={SortMoney} />
                <Stack.Screen name="Theo dõi đơn hàng" component={OrderTracking} />
                <Stack.Screen name="ReviewStack" component={ReviewStack}
                    options={{
                        title: 'Đánh giá',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'left',
                        headerTitleStyle: { fontSize: 18, },
                    }}
                />
                <Stack.Screen name="Sort" component={Sort} />
                <Stack.Screen name="Cart" component={Cart}
                    options={{
                        title: 'Giỏ hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, },
                    }}
                />
                <Stack.Screen name="OrderConfirm" component={OrderConfirm}
                    options={{
                        title: 'Xác nhận đơn hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, },
                    }}
                />
                <Stack.Screen name="Address" component={Address} 
                    options={{
                        title: 'Thay đổi địa chỉ',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, },
                    }}
                />
                <Stack.Screen name="AddAddress" component={AddAddress} 
                    options={{
                        title: 'Thêm địa chỉ mới',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, },
                    }}
                />
                <Stack.Screen name="OrderStatus" component={OrderStatus}
                    options={{
                        headerShown: false,
                    }}
                />
                <Stack.Screen name="Trang chủ" component={Home} options={{ headerShown: false, }} />
            </Stack.Navigator>
        </NavigationContainer>
    )
}

const VoucherStack = () => {
    return (
        <NavigationContainer independent={true}>
            <Stack.Navigator initialRouteName='Voucher'>
                <Stack.Screen name='Voucher' component={Voucher}
                    options={{ headerShown: false }}
                />
                <Stack.Screen name='VoucherDescription' component={VoucherDescription} />
                <Stack.Screen name='ChooseSuccessfully' component={ChooseSuccessfully} />
                <Stack.Screen name="Cart" component={Cart}
                    options={{
                        title: 'Giỏ hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, paddingBottom: 10, },
                        headerLeftContainerStyle: { paddingBottom: 10, },
                    }}
                />
                <Stack.Screen name="OrderConfirm" component={OrderConfirm}
                    options={{
                        title: 'Xác nhận đơn hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, paddingBottom: 10, },
                        headerLeftContainerStyle: { paddingBottom: 10, },
                    }}
                />
                <Stack.Screen name="AddAddress" component={AddAddress} />
                <Stack.Screen name="OrderTracking" component={OrderTracking}
                    options={{
                        title: 'Theo dõi đơn hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, paddingBottom: 10, },
                        headerLeftContainerStyle: { paddingBottom: 10, },
                    }}
                />
            </Stack.Navigator>
        </NavigationContainer>
    );
}

const ProfileStack = () => {
    return (
        <NavigationContainer
            independent={true}
        >
            <Stack.Navigator initialRouteName='Account'
                screenOptions={{ headerShown: false }}
            >
                <Stack.Screen name='Account' component={Account} />
                <Stack.Screen name='ThongTin' component={ThongTin} />
                <Stack.Screen name='DoiMatKhau' component={DoiMatKhau} />
                <Stack.Screen name='XacThuc' component={XacThuc} />
                <Stack.Screen name="OrderTracking" component={OrderTracking}
                    options={{
                        headerShown: true,
                        title: 'Theo dõi đơn hàng',
                        headerStyle: {
                            height: 60,
                        },
                        headerTitleAlign: 'center',
                        headerTitleStyle: { fontSize: 18, },
                    }}
                />
            </Stack.Navigator>
        </NavigationContainer>
    );
}


const ReviewStack = () => {
    return (
        <NavigationContainer independent={true}>
            <Stack.Navigator screenOptions={{ headerShown: false }}>
                <Stack.Screen name='ReviewDescription' component={ReviewDescription} />
            </Stack.Navigator>
        </NavigationContainer>
    );
}

export { HomeStack, MenuStack, VoucherStack, ProfileStack, ReviewStack, };