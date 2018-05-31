package com.example.mypar.gift.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.example.mypar.gift.R;

public class FAQ_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_);

        SpannableString s1 = new SpannableString(" · GIFT란 무엇인가?\n");
        SpannableString s2 = new SpannableString("환영합니다. GIFT서비스를 사용해 주셔서 감사합니다. GIFT for Mobile은 공식 그룹 SNS 및 온라인 플랫폼으로 사람이 네트워크에서 자신의 연결을 관리하도록 합니다. GIFT는 ADMIN-CENTERED 그룹 메신저 서비스로 무료로 그룹을 통해 휴대전화를 통해 연락 할 수 있습니다. 이 서비스를 사용하면 친구, 직장 동료 및 기타 개인과 함께 고유 한 그룹을 만들 수 있다.\n" +
                "관리자와 회원은 그룹에서 명확하게 구분되며, 회원에게 관련성 있는 맞춤 콘텐츠 만 제공 할 수 있습니다.\n\n");
        SpannableString s3 = new SpannableString(" · 누가 GIFT를 만들었나요?\n");
        SpannableString s4 = new SpannableString("광운대학교에서 산학연계에 참여한 팀(22세기로)\n" +
                "팀장 민세기 외4명(신진호, 박민규, 강윤구, 나승학)\n\n");
        SpannableString s5 = new SpannableString(" · 개인 정보 정책\n");
        SpannableString s6 = new SpannableString("개인 정보를 안전하게 보호하는 것이 우리의 최우선 과제 중 하나입니다. GIFT는 서비스를 원활하게 제공한다는 목표에 동의하는 목적 및 범위 내에서 귀하의 개인 정보를 사용합니다.\n" +
                "GIFT는 관력 법률 및 규정에서 별 도로 요구하거나 동의 한 경우를 제외하고는 귀하의 개인 정보를 제 3자에게 공개하지 않으므로 안심할 수 있습니다. 우리는 귀하가 귀하의 개인 정보 및 기타 관련 세부 사항을 보호하는 방법을 쉽게 이해할 수 있도록 GIFT가 제공하는 모든 서비스에 적용되는 본 개인정보 보호 정책을 개발 했습니다. 개인 정보 보호 정책은 일반인에게 항상 공개되어 있으므로 필요할 때마다 개인 정보 보호 정책을 읽을 수 있습니다. 개인정보 보호 정책에 대한 변경 사항은 귀하에게 그러한 변경 사항에 대한 세부 사항 및 이유를 이해하도록 통지됩니다.\n" +
                "GIFT는 귀하의 개인 정보를 다른 사람들과 공유하지 않습니다. 귀하의 전화번호와 이메일 주소는 다른 그룹 회원들로부터 항상 비공개로 유지됩니다.\n\n");
        SpannableString s7 = new SpannableString(" · 가입/추가\n");
        SpannableString s8 = new SpannableString("누구나 그룹 구성원이 될 수 있습니다.\n" +
                "그룹의 구성원으로서 신뢰할 수 있는 연결 또는 그룹의 네트워크에서 모든 정보를 받습니다.\n\n");
        SpannableString s9 = new SpannableString(" · 그룹 만들기\n");
        SpannableString s10 = new SpannableString("누구나 관리자로 그룹을 만들 수 있습니다. 누군가 이 그룹에 join하려고 가입 신청이 오면 관리자로서 승인을 하면다면 그룹의 구성원이 됩니다.\n\n");
        SpannableString s11 = new SpannableString(" · 시작하기\n");
        SpannableString s12 = new SpannableString("Android 용 GIFT는 휴대 전화의 인터넷 연결을 사용하여 다른 GIFT 사용자와 통신하는 정식 그룹 SNS 및 온라인 플랫폼입니다.\n" +
                "GIFT를 다운로드 하여 설치하기 전에 GIFT가 휴대 전화 유형을 지원하는지 확인 하십시오.\n" +
                "가입을 위해 E-mail, Password, name, Student ID, Phone number, 국적을 입력해야 합니다. 사진은 선택 사항입니다. 당신은 그룹을 만들고, 가입하고 싶은 그룹에 Join신청을 해서 허가가 되면 그룹의 멤버가 되어 원하는 글을 공유할 수 있습니다.\n\n");
        SpannableString s13 = new SpannableString(" · 개인정보 변경\n");
        SpannableString s14 = new SpannableString("내 이미지, 이름, 학번, 핸드폰번호, 국적 등을 바꿀 수 있습니다.\n\n");
        SpannableString s15 = new SpannableString(" · 계정관리\n");
        SpannableString s16 = new SpannableString("로그아웃과 계정삭제를 할 수 있습니다.\n\n");
        SpannableString s17 = new SpannableString(" · 그룹관리\n");
        SpannableString s18 = new SpannableString("관리자만 할 수 있습니다. (추방기능과, 타인으로부터 JOIN 승낙 및 거절 등)\n\n");
        SpannableString s19 = new SpannableString(" · 그룹탈퇴\n");
        SpannableString s20 = new SpannableString("어떤 이유로든 그룹을 떠나거나 끝낼 수 있습니다. 관리자 또한 그룹의 구성원의 허락 없이 떠날 수 있습니다.(그룹을 떠날 때 회원에게 통지 되지 않습니다)\n");


        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s1.setSpan(new StyleSpan(Typeface.BOLD), 0, s1.length(), flag);
        s1.setSpan(new AbsoluteSizeSpan(50), 0, s1.length(), flag);
        s1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s1.length(), flag);

        s3.setSpan(new StyleSpan(Typeface.BOLD), 0, s3.length(), flag);
        s3.setSpan(new AbsoluteSizeSpan(50), 0, s3.length(), flag);
        s3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s3.length(), flag);

        s5.setSpan(new StyleSpan(Typeface.BOLD), 0, s5.length(), flag);
        s5.setSpan(new AbsoluteSizeSpan(50), 0, s5.length(), flag);
        s5.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s5.length(), flag);

        s7.setSpan(new StyleSpan(Typeface.BOLD), 0, s7.length(), flag);
        s7.setSpan(new AbsoluteSizeSpan(50), 0, s7.length(), flag);
        s7.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s7.length(), flag);

        s9.setSpan(new StyleSpan(Typeface.BOLD), 0, s9.length(), flag);
        s9.setSpan(new AbsoluteSizeSpan(50), 0, s9.length(), flag);
        s9.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s9.length(), flag);

        s11.setSpan(new StyleSpan(Typeface.BOLD), 0, s11.length(), flag);
        s11.setSpan(new AbsoluteSizeSpan(50), 0, s11.length(), flag);
        s11.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s11.length(), flag);

        s13.setSpan(new StyleSpan(Typeface.BOLD), 0, s13.length(), flag);
        s13.setSpan(new AbsoluteSizeSpan(50), 0, s13.length(), flag);
        s13.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s13.length(), flag);

        s15.setSpan(new StyleSpan(Typeface.BOLD), 0, s15.length(), flag);
        s15.setSpan(new AbsoluteSizeSpan(50), 0, s15.length(), flag);
        s15.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s15.length(), flag);

        s17.setSpan(new StyleSpan(Typeface.BOLD), 0, s17.length(), flag);
        s17.setSpan(new AbsoluteSizeSpan(50), 0, s17.length(), flag);
        s17.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s17.length(), flag);

        s19.setSpan(new StyleSpan(Typeface.BOLD), 0, s19.length(), flag);
        s19.setSpan(new AbsoluteSizeSpan(50), 0, s19.length(), flag);
        s19.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s19.length(), flag);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s1);
        builder.append(s2);
        builder.append(s3);
        builder.append(s4);
        builder.append(s5);
        builder.append(s6);
        builder.append(s7);
        builder.append(s8);
        builder.append(s9);
        builder.append(s10);
        builder.append(s11);
        builder.append(s12);
        builder.append(s13);
        builder.append(s14);
        builder.append(s15);
        builder.append(s16);
        builder.append(s17);
        builder.append(s18);
        builder.append(s19);
        builder.append(s20);

        TextView textV1= findViewById(R.id.textView2);
        textV1.setText(builder);
        textV1.setMovementMethod(LinkMovementMethod.getInstance());

    }
}