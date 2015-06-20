package com.appetizerz.jean.utils;

/**
 * Created by david on 15-03-13.
 */
public class FixtureViewHelper {

/*    public FixtureViewHelper(ViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    public ViewHelper getViewHelper() {
        return viewHelper;
    }*/
/*
    private ViewHelper viewHelper;
    private TeamViewModel teamViewModel;
    private View.OnClickListener createFixtureItemClickedListener(final FixtureModel itemClicked){


        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getDataHelper().isClickable(v.getId()))
                    fixtureItemClickAction(itemClicked);
            }
        };
    }


    public RelativeLayout createFixtureForOneDate(List<FixtureModel> fixtureGroup){

        RelativeLayout fixtureGroupContainer = (RelativeLayout)getViewHelper().createViewFromInflator(R.layout.view_fixture_grouped);
        LinearLayout fixtureHolder = (LinearLayout) fixtureGroupContainer.findViewById(R.id.fixtureRowContainer);

        for(FixtureModel item : fixtureGroup){
            MitooEnum.FixtureStatus fixtureType = item.getFixtureType();
            if(fixtureType != MitooEnum.FixtureStatus.DELETED){
                fixtureHolder.addView(createFixtureRow(item));
            }
        }
        setDateForFixtureGroup(fixtureGroupContainer, fixtureGroup);
        return fixtureGroupContainer;
    }

    public void setDateForFixtureGroup(RelativeLayout fixtureGroupContainer , List<FixtureModel> fixtureGroup){

        if(fixtureGroup.size()>0){
            TextView dateTextView = (TextView) fixtureGroupContainer.findViewById(R.id.dateTextField);
            String fixtureDate = fixtureGroup.get(0).getLongDisplayableDate();
            dateTextView.setText(fixtureDate);
        }
    }

    public RelativeLayout createFixtureRow(FixtureModel fixture){

        RelativeLayout fixtureRow = (RelativeLayout)getViewHelper().createViewFromInflator(R.layout.view_fixture_row);
        customizeFixtureRow(fixtureRow, fixture);
        return fixtureRow;
    }

    private void setUpFixtureRowTeamTextView(View row, FixtureModel fixture){

        TextView leftTeamTextView = (TextView)row.findViewById(R.id.leftTeamName);
        TextView rightTeamTextView = (TextView)row.findViewById(R.id.rightTeamName);

        Team homeTeam = getDataHelper().getTeam(fixture.getFixture().getHome_team_id());
        Team awayTeam = getDataHelper().getTeam(fixture.getFixture().getAway_team_id());

        setUpTeamName(homeTeam, leftTeamTextView);
        setUpTeamName(awayTeam, rightTeamTextView);

    }

    private void setUpTeamName(Team team ,TextView textView) {

        getTeamViewModel().setUpTeamName(team , textView);

    }

    private void setUpFixtureStamp(View row, FixtureModel fixture){

        RelativeLayout alphaContainer = (RelativeLayout) row.findViewById(R.id.alphaContainer);
        ImageView stampView= (ImageView) row.findViewById(R.id.stampIcon);
        //IF THERE IS AN IMAGE, WE CLEAR IT, REFACTOR LATER
        boolean imageExists = true;
        if(imageExists){
            stampView.setImageResource(android.R.color.transparent);
            alphaContainer.setAlpha(getActivity().getDataHelper().getFloatValue(R.dimen.regular_alpha));
        }
        float alphaValue = getActivity().getDataHelper().getFloatValue(R.dimen.low_alpha);
        switch(fixture.getFixtureType()){

            case ABANDONED:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.abandonned_stamp);
                break;
            case VOID:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.void_stamp);
                break;
            case POSTPONED:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.postponed_stamp);
                break;
            case CANCELED:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.cancelled_stamp);
                break;
            case RESCHEDULED:
                alphaContainer.setAlpha(alphaValue);
                stampView.setImageResource(R.drawable.rescheduled_stamp);
                break;
            default:
                break;

        }
    }

    private void setUpFixtureTeamIcons(View row , FixtureModel fixture ) {

        ImageView leftTeamIcon = (ImageView) row.findViewById(R.id.leftTeamIcon);
        ImageView rightTeamIcon = (ImageView) row.findViewById(R.id.rightTeamIcon);
        Team homeTeam = getDataHelper().getTeam(fixture.getFixture().getHome_team_id());
        Team awayTeam = getDataHelper().getTeam(fixture.getFixture().getAway_team_id());
        loadTeamIcon(leftTeamIcon, homeTeam);
        loadTeamIcon(rightTeamIcon, awayTeam);

    }

    private void loadTeamIcon(ImageView imageView, Team team){

        getTeamViewModel().loadTeamIcon(imageView,team);
    }

    private void setUpFixtureCenterText(View row , FixtureModel fixture ) {

        TextView centerText = (TextView) row.findViewById(R.id.middleTextField);
        MitooEnum.TimeFrame fixtureTimeFrame = getDataHelper().getTimeFrame(fixture.getFixtureDate());
        switch (fixtureTimeFrame) {
            case FUTURE:
                centerText.setTextAppearance(getActivity(), R.style.schedulePageTimeText);
                centerText.setText(fixture.getDisplayableTime());
                break;
            case PAST:
                centerText.setTextAppearance(getActivity(), R.style.schedulePageScoreText);
                centerText.setText(fixture.getDisplayableScore());
                break;
            default:
                break;
        }
    }

    public void customizeFixtureRow(View row , FixtureModel fixture){

        setUpFixtureRowTeamTextView(row, fixture);
        setUpFixtureStamp(row, fixture);
        setUpFixtureCenterText(row, fixture);
        setUpFixtureTeamIcons(row, fixture);
        setUpFixtureOnClickListener(row, fixture);

    }

    private void setUpFixtureOnClickListener(View row, FixtureModel fixture){

        row.setOnClickListener(createFixtureItemClickedListener(fixture));
    }

    public TeamViewModel getTeamViewModel() {
        if(teamViewModel ==null){
            teamViewModel = new TeamViewModel(getViewHelper());
        }
        return teamViewModel;
    }

    private MitooActivity getActivity(){
        return getViewHelper().getActivity();
    }

    private DataHelper getDataHelper(){
        return getViewHelper().getActivity().getDataHelper();
    }

    private void fixtureItemClickAction(FixtureModel fixture){

        FragmentChangeEvent event = FragmentChangeEventBuilder
                .getSingletonInstance()
                .setFragmentID(R.id.fragment_fixture)
                .setBundle(createBundle(fixture))
                .build();
        BusProvider.post(event);

    }

    private Bundle createBundle(FixtureModel fixture){
        Bundle bundle = new Bundle();
        bundle.putInt(getFixtureIdKey(), fixture.getFixture().getId());
        return bundle;
    }

    private String getFixtureIdKey(){
        return getActivity().getString(R.string.bundle_key_fixture_id_key);
    }*/
}
