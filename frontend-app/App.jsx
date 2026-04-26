import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";

import GuardianLoginPage from "./src/pages/auth/GuardianLoginPage";
import GuardianMainPage from "./src/pages/guardian/GuardianMainPage";
import ReportPage from "./src/pages/guardian/ReportPage";
import ConsentPage from "./src/pages/guardian/ConsentPage";
import VisitApplyPage from "./src/pages/guardian/VisitApplyPage";
import NoticePage from "./src/pages/guardian/NoticePage";
import CalendarPage from "./src/pages/guardian/CalendarPage";
import PaymentPage from "./src/pages/guardian/PaymentPage";
import LiveCheckPage from "./src/pages/guardian/LiveCheckPage";
import ChatbotPage from "./src/pages/guardian/ChatbotPage";
import MyPage from "./src/pages/guardian/MyPage";
import GalleryPage from "./src/pages/guardian/GalleryPage";
import ActivePhotoGalleryPage from "./src/pages/guardian/activePhoto/GalleryPage";
import GuardianMorePage from "./src/pages/guardian/activePhoto/GuardianMorePage";

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="GuardianLogin">
        <Stack.Screen name="GuardianLogin" component={GuardianLoginPage} options={{ title: "로그인" }} />
        <Stack.Screen name="GuardianMain" component={GuardianMainPage} options={{ headerShown: false }} />
        <Stack.Screen name="Report" component={ReportPage} options={{ title: "보고서 확인" }} />
        <Stack.Screen name="Consent" component={ConsentPage} options={{ title: "동의서 확인" }} />
        <Stack.Screen name="VisitApply" component={VisitApplyPage} options={{ title: "면회 신청" }} />
        <Stack.Screen name="Notice" component={NoticePage} options={{ title: "공지사항" }} />
        <Stack.Screen name="Calendar" component={CalendarPage} options={{ title: "달력" }} />
        <Stack.Screen name="Payment" component={PaymentPage} options={{ title: "수납" }} />
        <Stack.Screen name="LiveCheck" component={LiveCheckPage} options={{ title: "실시간" }} />
        <Stack.Screen name="Chatbot" component={ChatbotPage} options={{ title: "챗봇" }} />
        <Stack.Screen name="MyPage" component={MyPage} options={{ title: "마이페이지" }} />
        <Stack.Screen name="Gallery" component={GalleryPage} options={{ title: "갤러리"}} />
        <Stack.Screen
          name="ActivePhotoGallery"
          component={ActivePhotoGalleryPage}
          options={{ title: "활동 사진" }}
        />
        <Stack.Screen
          name="GalleryMore"
          component={GuardianMorePage}
          options={{ headerShown: false }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
