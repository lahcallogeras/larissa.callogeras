//
//  temp.m
//  conex
//
//  Created by Gleison on 08/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import "temp.h"

@implementation temp

@end

#import <UIKit/UIKit.h>
#import "ContatoDAO.h"

@interface ContatoController : UIViewController{
    
    
    IBOutlet UIImageView *image;
    NSMutableArray *lista;
    
}

@property(nonatomic,retain) NSMutableArray *lista;

@end





- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documents = [paths objectAtIndex:0];
    NSString *finalPath = [documents stringByAppendingPathComponent:@"gleison.jpeg"];
    NSData *pngData = [NSData dataWithContentsOfFile:finalPath];
    UIImage *myImage = [UIImage imageWithData:pngData];
    image.image = myImage;
    
    
    
    ContatoDAO *listaContato =[[ContatoDAO alloc] init];
    self.lista = [listaContato getContatos];
    
    Contato *contatoObj = [self.lista objectAtIndex:4];
    
    
    
    
    
    int teste = [lista count];
    NSLog(contatoObj.nomeUsuCtt);
    
    
    
    
}
