//
//  ContatoController.h
//  conex
//
//  Created by Gleison on 08/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ContatoController : UITableViewController{
    
    
    //IBOutlet UIImageView *image;
    NSMutableArray *lista;

    
}

@property(nonatomic,retain) NSMutableArray *lista;
@property (nonatomic, strong) IBOutlet UITableView *contatosTableView;


-(IBAction)btnSair:(id)sender;
-(IBAction)BtnDesconectar:(id)sender;
- (IBAction)gerenciarContatos:(id)sender;

@end
