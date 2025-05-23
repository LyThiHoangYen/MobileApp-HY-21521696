import React from "react";
import { View, Text, Image, Dimensions } from "react-native";
import { useNavigation } from "@react-navigation/native";
import { TouchableOpacity } from "react-native-gesture-handler";

export default function EachNoti(){
    const navigation = useNavigation();
    return(
        <View>
            <View style={{
                flexDirection:'row',
                alignItems:'center',
                justifyContent:'space-between',
                height:60,
                width: Dimensions.get('window').width,
                backgroundColor:'white'
            }}>
                <TouchableOpacity onPress={() => navigation.goBack()}>
                    <Image source={require('../../assets/Back.png')} style={{marginLeft:20}} />
                </TouchableOpacity>
                <Text style={{
                    fontSize:27, 
                    fontWeight:'bold',
                    }}>
                    Thông báo
                </Text>
                <Image source={require('../../assets/Seen.png')} style={{marginRight:20}} />
            </View>
            <View style={{
                backgroundColor:'#E5E1E1',
                height:75,
                flexDirection:'row',
                width: Dimensions.get('window').width,
            }}>
                <Image source={require('../../assets/DeliveryIcon.png')} style={{marginLeft:10, alignSelf:'center', width:60, height:60}} />
                <Text style={{width:Dimensions.get('window').width - 180, fontSize:16, marginLeft:20, alignSelf:'center'}}>Đơn hàng của bạn đang trên đường giao</Text>
                <Text style={{left:5, top:5}}>02/07/2023</Text>
            </View>
        </View>
        
    );
}
