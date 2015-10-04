//
//  MensagemController.h
//  conex
//
//  Created by Gleison on 10/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <UIKit/UIKit.h>
@interface MensagemController : UITableViewController{    
    NSMutableArray *lista;
    IBOutlet UITableView *msgTableView;
    IBOutlet UIButton *btnEnviar;
    IBOutlet UITextField *txtMsg;

}


@property(nonatomic,retain) NSMutableArray *lista;

- (IBAction)doneTeclado:(id)sender;
- (IBAction)btnEnviarClicked:(id)sender;
- (IBAction)btnVoltar:(id)sender;
- (IBAction)btnExcluir:(id)sender;

- (IBAction)ativaTeclado:(id)sender;
- (IBAction)desativaTeclado:(id)sender;

@end
