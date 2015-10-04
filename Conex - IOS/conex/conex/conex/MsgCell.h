//
//  MsgCell.h
//  conex
//
//  Created by Larii on 11/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MsgCell : UITableViewCell{

    IBOutlet UILabel *lblMsgLeft;
    IBOutlet UIImageView *imgFotoLeft;
    IBOutlet UILabel *lblMsgRight;
    IBOutlet UIImageView *imgFotoRight;
    IBOutlet UILabel *lblDataLeft;
    IBOutlet UILabel *lblDataRight;
    
}


@property (nonatomic, retain) UILabel *lblMsgLeft;
@property (nonatomic, retain) UIImageView *imgFotoLeft;
@property (nonatomic, retain) UILabel *lblMsgRight;
@property (nonatomic, retain) UIImageView *imgFotoRight;
@property (nonatomic, retain) UILabel *lblDataLeft;
@property (nonatomic, retain) UILabel *lblDataRight;

@end
